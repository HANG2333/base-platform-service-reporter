package com.jkr.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 请填写说明
 *
 * @author 纪维元
 * @since 2022年07月18 11:42:09
 **/
@Slf4j
public class ParamsHttpServletRequestWrapper extends HttpServletRequestWrapper {

    @Getter
    @Setter
    private String body;

    public ParamsHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        if (request.getHeader(HttpHeaders.CONTENT_TYPE) != null) {
            String header = request.getHeader(HttpHeaders.CONTENT_TYPE).toLowerCase();
            if (!StringUtils.isBlank(header) && header.contains(MediaType.APPLICATION_JSON_VALUE)) {
                try {
                    this.body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
                } catch (Exception e) {
                    log.error("接收body出现异常");
                }
            }
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        String header = super.getHeader(HttpHeaders.CONTENT_TYPE).toLowerCase();
        //非json类型，直接返回
        if (!header.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return super.getInputStream();
        }
        //为空，直接返回
        if (StringUtils.isBlank(body)) {
            return super.getInputStream();
        }

        final ByteArrayInputStream bis = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            @SneakyThrows
            public int read() {
                return bis.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }


}

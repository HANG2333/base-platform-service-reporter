package com.jkr.filter;

import com.alibaba.fastjson2.JSON;
import com.jkr.common.aspect.RequestLogAspect;
import com.jkr.common.aspect.ValidResultAspect;
import com.jkr.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * 请填写说明
 *
 * @author 纪维元
 * @since 2022年09月14 09:20:11
 **/
@Slf4j
public class CsrfInterceptor implements HandlerInterceptor {

    @Value("${refererUrl}")
    private String refererUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String urlStr = request.getHeader(HttpHeaders.REFERER);
        if (!StringUtils.hasLength(urlStr)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        } else {
            URL url = new URL(urlStr);
            String host = url.getHost();
            List<String> securityUrl = Arrays.asList(refererUrl.split(","));
            if (!securityUrl.contains(host)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = response.getWriter();
                out.append(JSON.toJSONString(ResponseData.error(401, "您正在进行非法访问。")));
                log.info("来访ip:{},访问uri:{},来访referer地址:{}", RequestLogAspect.getRemoteAddr(request), request.getRequestURI(), urlStr);
                return false;
            } else {
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }
        }
    }
}

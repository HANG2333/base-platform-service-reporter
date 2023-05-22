package com.jkr.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jkr.common.model.ResponseData;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 请填写说明
 *
 * @author 纪维元
 * @since 2022年09月14 14:30:49
 **/
public class ParamsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String method = req.getMethod();
        if(HttpMethod.POST.name().equalsIgnoreCase(method)){
            if(req.getHeader(HttpHeaders.CONTENT_TYPE) != null){
                String header = req.getHeader(HttpHeaders.CONTENT_TYPE).toLowerCase();
                if (!StringUtils.isBlank(header) &&  header.contains(MediaType.APPLICATION_JSON_VALUE)) {
                    String json = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
                    if (!StringUtils.isBlank(json)) {
                        Object o = JSON.parse(json);
                        if(o instanceof JSONObject){
                            JSONObject jsonObject = (JSONObject)o;
                            if(jsonObject.containsKey("is_admin") || jsonObject.containsKey("role") || jsonObject.containsKey("isadmin")){
                                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                                res.setCharacterEncoding("UTF-8");
                                res.setContentType("application/json; charset=utf-8");
                                PrintWriter out = res.getWriter();
                                out.append(JSON.toJSONString(ResponseData.error(401, "您正在进行非法访问。")));
                                return;
                            }
                        }
                    }
                    ParamsHttpServletRequestWrapper paramsHttpServletRequestWrapper =  new ParamsHttpServletRequestWrapper(req);
                    paramsHttpServletRequestWrapper.setBody(json);
                    request = paramsHttpServletRequestWrapper;
                }
            }
        }
        chain.doFilter(request, response);
    }
}

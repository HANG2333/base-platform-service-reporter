package com.jkr.config;

import com.jkr.filter.CsrfInterceptor;
import com.jkr.filter.ParamsFilter;
import com.jkr.filter.UserAuthRestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import java.util.List;

/**
 * @Author：jikeruan
 * @Description:
 * @Date: 2019/11/8 8:47
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${ignored}")
    private List<String> ignored;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(csrfInterceptor()).addPathPatterns("/**").excludePathPatterns(ignored);
        // token拦截器 获取用户信息
        registry.addInterceptor(userAuthRestInterceptor()).addPathPatterns("/**").excludePathPatterns(ignored);
    }

    @Bean
    public UserAuthRestInterceptor userAuthRestInterceptor() {
        return new UserAuthRestInterceptor();
    }

    @Bean
    public CsrfInterceptor csrfInterceptor() {
        return new CsrfInterceptor();
    }

    @Bean
    public FilterRegistrationBean paramsFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new ParamsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("paramsFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return registration;
    }

}

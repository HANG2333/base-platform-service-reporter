package com.jkr;


import org.minbox.framework.api.boot.autoconfigure.swagger.annotation.EnableApiBootSwagger;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.PostConstruct;

/**
 * @author
 */
@SpringBootApplication
@EnableApiBootSwagger
@MapperScan(basePackages = {"com.jkr.modules.**.mapper"})
@EnableWebSocket
@EnableScheduling
public class WebApplication implements WebSocketConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @PostConstruct
    public void myLog() {
        logger.trace("WebSocketConfigurer is Published");
    }

    /**
     * @title: registerWebSocketHandlers
     * @author: DaiFuyou
     * @date: 2020/5/22
     * @description:注册websocket的处理器
     * @param: [registry]
     * @return: void
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    }


    /**
     * method_name: taskScheduler
     * create_user: jikeruan
     * create_date: 2020/6/17
     * create_time: 9:48
     * describe: 由于WebSocket与定时冲突需手动创建
     * param : []
     * return : org.springframework.scheduling.TaskScheduler
     */
    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler =new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

    /**
     * method_name: webServerFactory
     * create_user: zhaosongming
     * create_date: 2020-09-09
     * create_time: 04:40:58
     * describe: 处理查询特殊字符报错问题
     * @param
     * @return org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\"));
        return factory;
    }
}

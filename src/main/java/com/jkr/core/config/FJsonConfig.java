package com.jkr.core.config;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @Author：jikeruan
 * @Description: fastJson 配置
 * @Date: 2019/9/6 14:51
 */
@Configuration
public class FJsonConfig {

    @Bean
    public HttpMessageConverter configureMessageConverters() {

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        // 反序列化
        DeserializationConfig config = objectMapper.getDeserializationConfig();
        config.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setConfig(config);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        // 序列化
        SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
        SerializerFactory factory = objectMapper.getSerializerFactory();
        factory.withSerializerModifier(new MyBeanSerializerModifier());
        objectMapper.setSerializerFactory(factory);
        objectMapper.setConfig(serializationConfig);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        converter.setObjectMapper(objectMapper);

        converter.setDefaultCharset(Charset.forName("UTF-8"));
        List<MediaType> mediaTypeList = new ArrayList<>();
        // 解决中文乱码问题，相当于在Controller上的@RequestMapping中加了个属性produces = "application/json"
        mediaTypeList.add(MediaType.ALL);
        converter.setSupportedMediaTypes(mediaTypeList);


        return converter;
    }

}



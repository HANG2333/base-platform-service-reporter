package com.jkr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class RedisSubListenerConfig {
    @Bean
    ChannelTopic topic() {
        return new ChannelTopic(Global.NEWS_REMINDER_PUSH_CHANNEL);
    }
}

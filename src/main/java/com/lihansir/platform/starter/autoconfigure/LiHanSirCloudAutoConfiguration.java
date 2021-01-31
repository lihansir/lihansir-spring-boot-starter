/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.lihansir.platform.starter.utils.RedisOpsUtil;
import com.lihansir.platform.starter.utils.TokenOpsUtil;

import io.jsonwebtoken.Claims;

/**
 * autoconfigure main class
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
@Configuration
public class LiHanSirCloudAutoConfiguration {

    /**
     * redis autoconfigure class
     */
    @Configuration
    @ConditionalOnClass(RedisOperations.class)
    protected static class CloudRedisAutoConfiguration {
        @Bean
        @Primary
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);
            jackson2JsonRedisSerializer.setObjectMapper(om);
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            template.setKeySerializer(stringRedisSerializer);
            template.setValueSerializer(jackson2JsonRedisSerializer);
            template.setHashKeySerializer(jackson2JsonRedisSerializer);
            template.setHashValueSerializer(jackson2JsonRedisSerializer);
            template.afterPropertiesSet();
            return template;
        }

        @Bean
        public RedisOpsUtil redisOpsUtil(RedisTemplate<String, Object> redisTemplate) {
            return new RedisOpsUtil(redisTemplate);
        }
    }

    @Configuration
    @ConditionalOnClass(Claims.class)
    protected static class CloudTokenAutoConfiguration {
        @Bean
        public TokenOpsUtil tokenOpsUtil() {
            return new TokenOpsUtil();
        }
    }

}

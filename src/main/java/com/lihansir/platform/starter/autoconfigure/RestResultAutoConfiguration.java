/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.lihansir.platform.starter.advice.RestResultHandlerAdvice;
import com.lihansir.platform.starter.handler.GlobalExceptionHandler;
import com.lihansir.platform.starter.resolver.GlobalErrorViewResolver;

/**
 * Unified response configuration class
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
@Configuration(proxyBeanMethods = false)
public class RestResultAutoConfiguration {

    @Bean
    public RestResultHandlerAdvice restResultHandlerAdvice() {
        return new RestResultHandlerAdvice();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public GlobalErrorViewResolver globalErrorViewResolver() {
        return new GlobalErrorViewResolver();
    }

}

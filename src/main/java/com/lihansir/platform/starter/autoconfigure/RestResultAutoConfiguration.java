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
 * <p>
 * Unified response configuration class
 * </p>
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 * @date Created in 2020/10/05 13:22
 */
@Configuration
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

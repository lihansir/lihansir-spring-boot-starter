/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.annotation;

import com.lihansir.platform.starter.autoconfigure.RestResultAutoConfiguration;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * <p>
 * Enable unified response
 * </p>
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 * @date Created in 2020/10/05 13:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
@Import(RestResultAutoConfiguration.class)
public @interface EnableRestResult {}

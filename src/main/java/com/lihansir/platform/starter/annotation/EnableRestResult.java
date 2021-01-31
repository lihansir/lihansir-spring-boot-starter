/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.annotation;

import com.lihansir.platform.starter.autoconfigure.RestResultAutoConfiguration;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * Enable unified response
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
@Import(RestResultAutoConfiguration.class)
public @interface EnableRestResult {}

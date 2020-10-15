/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Using unified response encapsulation
 * </p>
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 * @date Created in 2020/10/05 12:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface UseRestResult {}

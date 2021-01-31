/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.annotation;

import java.lang.annotation.*;

/**
 * Using unified response encapsulation
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface UseRestResult {}

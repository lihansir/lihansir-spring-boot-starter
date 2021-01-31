/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.annotation;

import java.lang.annotation.*;

/**
 * Ignore encapsulated response
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface IgnoreRestResult {}

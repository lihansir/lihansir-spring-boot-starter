package com.lihansir.platform.starter.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * CommonUtil
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
public class CommonUtil {

    public static ServletRequestAttributes getServletRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes)attributes;
    }

    private CommonUtil() {}

}

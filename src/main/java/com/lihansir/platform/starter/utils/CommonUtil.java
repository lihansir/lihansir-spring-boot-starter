package com.lihansir.platform.starter.utils;

import cn.hutool.core.util.StrUtil;
import com.lihansir.platform.common.rest.RestResult;
import com.lihansir.platform.starter.context.RestResultHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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

    public static String getTraceId() {
        String traceId = RestResultHolder.getTraceId();
        if (StrUtil.isNotBlank(traceId)) {
            return traceId;
        }
        return StrUtil.EMPTY;
    }

    public static String getBaseRequestUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getRemoteHost();
        String port = String.valueOf(request.getServerPort());
        String url = scheme + "://" + host;
        if (StrUtil.isNotBlank(port) && !"80".equals(port) && !"443".equals(port)) {
            url = url + ":" + port;
        }
        return url;
    }

    public static RestResult<Object> formatRestResult(RestResult<Object> result) {
        result.setTraceId(RestResultHolder.getTraceId());
        result.setHost(RestResultHolder.getHost());
        result.setShowType(RestResultHolder.getErrorShowType());
        return result;
    }

    private CommonUtil() {}

}

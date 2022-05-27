package com.lihansir.platform.starter.context;

import cn.hutool.core.util.StrUtil;

/**
 * RestResultHolder
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
public class RestResultHolder {

    private static final ThreadLocal<String> TRACE_ID = new InheritableThreadLocal<>();

    private static final ThreadLocal<String> HOST = new InheritableThreadLocal<>();

    /**
     * error display type： 0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
     */
    private static final ThreadLocal<Integer> ERROR_SHOW_TYPE = InheritableThreadLocal.withInitial(() -> 4);

    public static void setTraceId(String traceId) {
        TRACE_ID.set(traceId);
    }

    public static void setHost(String host) {
        HOST.set(host);
    }

    public static void setErrorShowType(int showType) {
        ERROR_SHOW_TYPE.set(showType);
    }

    public static String getTraceId() {
        String traceId = TRACE_ID.get();
        if (traceId == null) {
            traceId = StrUtil.EMPTY;
        }
        return traceId;
    }

    public static String getHost() {
        String host = HOST.get();
        if (host == null) {
            host = StrUtil.EMPTY;
        }
        return host;
    }

    public static Integer getErrorShowType() {
        return ERROR_SHOW_TYPE.get();
    }

    public static void remove() {
        TRACE_ID.remove();
        HOST.remove();
        ERROR_SHOW_TYPE.remove();
    }

    private RestResultHolder() {}

}

/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.resolver;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.lihansir.platform.common.code.CommonCode;
import com.lihansir.platform.common.constant.CommonConstant;
import com.lihansir.platform.starter.context.RestResultHolder;
import com.lihansir.platform.starter.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The error page throws an exception directly
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
public class GlobalErrorViewResolver implements ErrorViewResolver {

    private final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorViewResolver.class);

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        Objects.requireNonNull(CommonUtil.getServletRequestAttributes().getResponse()).setStatus(HttpStatus.OK.value());
        String traceId = RestResultHolder.getTraceId();
        String requestPath = (String)model.getOrDefault("path", "/");
        FastJsonJsonView jsonView = new FastJsonJsonView();
        Map<String, Object> resultModel = new LinkedHashMap<>();
        resultModel.put(CommonConstant.REST_RESPONSE_STATUS, false);
        resultModel.put(CommonConstant.REST_RESPONSE_ERROR_CODE, CommonCode.ERROR_URL.getErrorCode());
        resultModel.put(CommonConstant.REST_RESPONSE_ERROR_MESSAGE,
            CommonCode.ERROR_URL.getErrorMessage() + "，request path:【" + requestPath + "】");
        resultModel.put("traceId", traceId);
        resultModel.put("showType", RestResultHolder.getErrorShowType());
        resultModel.put("data", null);
        resultModel.put("host", RestResultHolder.getHost());
        LOGGER.error("Path handler not found, Path：【{}】,TraceId：【{}】", requestPath, traceId);
        return new ModelAndView(jsonView, resultModel);
    }
}

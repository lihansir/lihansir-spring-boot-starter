/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.advice;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.lihansir.platform.common.code.CommonCode;
import com.lihansir.platform.common.rest.RestResult;
import com.lihansir.platform.starter.annotation.IgnoreRestResult;
import com.lihansir.platform.starter.annotation.UseRestResult;
import com.lihansir.platform.starter.context.RestResultHolder;
import com.lihansir.platform.starter.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.Objects;

/**
 * Global unified response processing
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
@ControllerAdvice
public class RestResultHandlerAdvice implements ResponseBodyAdvice<Object> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return Objects.requireNonNull(methodParameter.getMethod()).getReturnType() != RestResult.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        int status = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse().getStatus();
        if (status == HttpStatus.NOT_FOUND.value()) {
            String requestPath = StrUtil.EMPTY;
            if (body instanceof Map) {
                Map<String, Object> errorData = (Map<String, Object>) body;
                requestPath = (String) errorData.getOrDefault("path", "/");
            }
            serverHttpResponse.setStatusCode(HttpStatus.OK);
            RestResult<Object> result = RestResult.failedWithErrorMessage(CommonCode.ERROR_URL.getErrorCode(),
                    CommonCode.ERROR_URL.getErrorMessage() + "，request path:【" + requestPath + "】");
            CommonUtil.formatRestResult(result);
            LOGGER.error("Path handler not found, Path：【{}】,TraceId：【{}】", requestPath, RestResultHolder.getTraceId());
            return result;
        }
        AnnotatedElement annotatedElement = methodParameter.getAnnotatedElement();
        IgnoreRestResult ignoreAnnotation = annotatedElement.getAnnotation(IgnoreRestResult.class);
        if (ignoreAnnotation != null) {
            return body;
        }
        UseRestResult useRestResponseType =
                methodParameter.getExecutable().getDeclaringClass().getAnnotation(UseRestResult.class);
        if (useRestResponseType != null) {
            return formatResponse(body, methodParameter, serverHttpResponse);
        }
        UseRestResult responseAnnotationMethod = annotatedElement.getAnnotation(UseRestResult.class);
        if (responseAnnotationMethod != null) {
            return formatResponse(body, methodParameter, serverHttpResponse);
        }
        return body;
    }

    /**
     * Format unified response results
     *
     * @param body            Response body
     * @param methodParameter methodParameter
     * @param response        Http response
     * @return Unified response object
     */
    private Object formatResponse(Object body, MethodParameter methodParameter, ServerHttpResponse response) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        RestResult<Object> restResponse = RestResult.builder().success(true).data(body).build();
        CommonUtil.formatRestResult(restResponse);
        Class<?> parameterType = methodParameter.getParameterType();
        if (String.class == parameterType) {
            return JSONObject.toJSONString(restResponse);
        }
        return restResponse;
    }

}

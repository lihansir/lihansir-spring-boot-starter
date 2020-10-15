/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.advice;

import com.alibaba.fastjson.JSONObject;
import com.lihansir.platform.common.code.CommonCode;
import com.lihansir.platform.common.rest.RestResult;
import com.lihansir.platform.starter.annotation.IgnoreRestResult;
import com.lihansir.platform.starter.annotation.UseRestResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

/**
 * <p>
 * Global unified response processing
 * </p>
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 * @date Created in 2020/10/05 12:35
 */
@ControllerAdvice
public class RestResultHandlerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return Objects.requireNonNull(methodParameter.getMethod()).getReturnType() != RestResult.class;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass,
        ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        int status = ((ServletServerHttpResponse)serverHttpResponse).getServletResponse().getStatus();
        if (status == HttpStatus.NOT_FOUND.value()) {
            serverHttpResponse.setStatusCode(HttpStatus.OK);
            return RestResult.failed(CommonCode.ERROR_URL);
        }
        AnnotatedElement annotatedElement = methodParameter.getAnnotatedElement();
        IgnoreRestResult ignoreAnnotation = annotatedElement.getAnnotation(IgnoreRestResult.class);
        if (ignoreAnnotation != null) {
            return body;
        }
        UseRestResult useRestResponseType =
            methodParameter.getExecutable().getDeclaringClass().getAnnotation(UseRestResult.class);
        if (useRestResponseType != null) {
            return formatResponse(body, serverHttpResponse);
        }
        UseRestResult responseAnnotationMethod = annotatedElement.getAnnotation(UseRestResult.class);
        if (responseAnnotationMethod != null) {
            return formatResponse(body, serverHttpResponse);
        }
        return body;
    }

    /**
     * <p>
     * Format unified response results
     * </p>
     *
     * @author <a href="https://www.lihansir.com">Li Han</a>
     * @date Created in 2020/10/05 12:44
     * @param body
     *            Response body
     * @param response
     *            Http response
     * @return Unified response object
     */
    private Object formatResponse(Object body, ServerHttpResponse response) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        RestResult<Object> restResponse =
            new RestResult<>(CommonCode.OK.getCode(), CommonCode.OK.getMsg(), JSONObject.toJSON(body));
        if (body instanceof String) {
            return JSONObject.toJSONString(restResponse);
        }
        return restResponse;
    }

}

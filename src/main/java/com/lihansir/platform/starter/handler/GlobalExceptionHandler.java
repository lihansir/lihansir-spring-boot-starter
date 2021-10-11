/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.handler;

import cn.hutool.core.util.ArrayUtil;
import com.lihansir.platform.common.code.CommonCode;
import com.lihansir.platform.common.exception.BusinessException;
import com.lihansir.platform.common.exception.ParamException;
import com.lihansir.platform.common.rest.RestResult;
import com.lihansir.platform.starter.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;

/**
 * Global exception interceptor
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Business logic processing exception
     *
     * @param e Exception
     * @return Unified response
     */
    @ExceptionHandler(BusinessException.class)
    public RestResult<Object> businessException(BusinessException e) {
        LOGGER.error("Business processing error，Error status code：【{}】,TraceId：【{}】,Cause of error：【{}】",
                e.getErrorCode(), CommonUtil.getTraceId(), e.getErrorMessage(), e);
        return CommonUtil.formatRestResult(RestResult.failedWithErrorMessage(e.getErrorCode(), e.getErrorMessage()));
    }

    /**
     * Abnormal parameter verification
     *
     * @param e Exception
     * @return Unified response
     */
    @ExceptionHandler(ParamException.class)
    public RestResult<Object> paramException(ParamException e) {
        LOGGER.error("Parameter verification error，Error status code：【{}】,TraceId：【{}】,Cause of error：【{}】",
                e.getErrorCode(), CommonUtil.getTraceId(), e.getErrorMessage(), e);
        return CommonUtil.formatRestResult(RestResult.failedWithErrorMessage(e.getErrorCode(), e.getErrorMessage()));
    }

    /**
     * MissingServletRequestParameterException
     *
     * @param e MissingServletRequestParameterException
     * @return Unified response
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RestResult<Object> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        LOGGER.error("MissingServletRequestParameterException: 【{}】,TraceId：【{}】", e.getMessage(),
                CommonUtil.getTraceId(), e);
        return CommonUtil.formatRestResult(
                RestResult.failedWithErrorMessage(CommonCode.PARAM_CHECK_ERROR.getErrorCode(), e.getMessage()));
    }

    /**
     * Request path error
     *
     * @return Unified response
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public RestResult<Object> handlerNoFoundException() {
        String requestUrl = CommonUtil.getServletRequestAttributes().getRequest().getRequestURI();
        LOGGER.error("Path handler not found, Path：【{}】,TraceId：【{}】", requestUrl, CommonUtil.getTraceId());
        return CommonUtil.formatRestResult(RestResult.failedWithErrorMessage(CommonCode.ERROR_URL.getErrorCode(),
                CommonCode.ERROR_URL.getErrorMessage() + "，request path:【" + requestUrl + "】"));
    }

    /**
     * Validated bind exception
     *
     * @param e Exception
     * @return Unified response
     */
    @ExceptionHandler(BindException.class)
    public RestResult<Object> validatedBindException(BindException e) {
        String errorMsg = ArrayUtil
                .join(e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(), ",");
        LOGGER.error("Custom validation exception：【{}】,TraceId：【{}】", errorMsg, CommonUtil.getTraceId(), e);
        return CommonUtil.formatRestResult(RestResult.failedWithErrorMessage(CommonCode.PARAM_CHECK_ERROR.getErrorCode(), errorMsg));
    }

    /**
     * Custom validation exception
     *
     * @param e Exception
     * @return Unified response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResult<Object> validExceptionHandler(MethodArgumentNotValidException e) {
        assert e.getBindingResult().getFieldError() != null;
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        LOGGER.error("Custom validation exception：【{}】,TraceId：【{}】", message, CommonUtil.getTraceId(), e);
        return CommonUtil.formatRestResult(RestResult.failedWithErrorMessage(CommonCode.PARAM_CHECK_ERROR.getErrorCode(), message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RestResult<Object> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        LOGGER.error("HttpMessageNotReadable exception：【{}】,TraceId：【{}】", e.getMessage(), CommonUtil.getTraceId(), e);
        return CommonUtil.formatRestResult(RestResult.failed(CommonCode.SERVLET_ERROR));
    }

    /**
     * Unsupported request method exception
     *
     * @param e Exception
     * @return Unified response
     */
    @ExceptionHandler(value = ServletException.class)
    public RestResult<Object> servletExceptionHandler(ServletException e) {
        LOGGER.error("Servlet Exception：【{}】,TraceId：【{}】", e.getMessage(), CommonUtil.getTraceId(), e);
        return CommonUtil.formatRestResult(RestResult.failedWithErrorMessage(CommonCode.SERVLET_ERROR.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(value = HttpMessageConversionException.class)
    public RestResult<Object> httpMessageConversionExceptionHandler(HttpMessageConversionException e) {
        LOGGER.error("HttpMessageConversionException：【{}】,TraceId：【{}】", e.getMessage(), CommonUtil.getTraceId(), e);
        return CommonUtil.formatRestResult(RestResult.failedWithErrorMessage(CommonCode.SERVLET_ERROR.getErrorCode(), e.getMessage()));
    }

    /**
     * Runtime exception
     *
     * @param e Exception
     * @return Unified response
     */
    @ExceptionHandler(value = RuntimeException.class)
    public RestResult<Object> runtimeExceptionHandler(RuntimeException e) {
        LOGGER.error("A runtime error occurred on the server，Cause of error：【{}】,TraceId：【{}】", e.getMessage(),
                CommonUtil.getTraceId(), e);
        return CommonUtil.formatRestResult(RestResult.failedWithErrorMessage(CommonCode.PROGRAM_EXECUTION_EXCEPTION.getErrorCode(), e.getMessage()));
    }

    /**
     * Illegal argument exception
     *
     * @param e Exception
     * @return Unified response
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public RestResult<Object> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        LOGGER.error("Error during inspection，Cause of error：【{}】,TraceId：【{}】", e.getMessage(),
                CommonUtil.getTraceId(), e);
        return CommonUtil.formatRestResult(
                RestResult.failedWithErrorMessage(CommonCode.ILLEGAL_ARGUMENT_ERROR.getErrorCode(), e.getMessage()));
    }

    /**
     * Exception
     *
     * @param e Exception
     * @return Unified response
     */
    @ExceptionHandler(Exception.class)
    public RestResult<Object> handleException(Exception e) {
        LOGGER.error("Exception：【{}】,TraceId：【{}】", e.toString(), CommonUtil.getTraceId(), e);
        return CommonUtil.formatRestResult(RestResult.failedWithErrorMessage(CommonCode.PROGRAM_EXECUTION_EXCEPTION.getErrorCode(), e.getMessage()));
    }

}

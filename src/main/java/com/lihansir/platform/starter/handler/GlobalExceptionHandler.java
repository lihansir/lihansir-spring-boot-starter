/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.lihansir.platform.common.code.CommonCode;
import com.lihansir.platform.common.exception.BusinessException;
import com.lihansir.platform.common.exception.ParamException;
import com.lihansir.platform.common.rest.RestResult;
import com.lihansir.platform.starter.utils.CommonUtil;

import cn.hutool.core.util.ArrayUtil;

/**
 * Global exception interceptor
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Business logic processing exception
     *
     * @param e
     *            Exception
     * @return Unified response
     */
    @ExceptionHandler(BusinessException.class)
    public RestResult<Object> businessException(BusinessException e) {
        logger.error("Business processing error，Error status code：【{}】,Cause of error：【{}】", e.getErrorCode(),
            e.getErrorMessage(), e);
        return RestResult.failedWithErrorMessage(e.getErrorCode(), e.getErrorMessage());
    }

    /**
     * Abnormal parameter verification
     *
     * @param e
     *            Exception
     * @return Unified response
     */
    @ExceptionHandler(ParamException.class)
    public RestResult<Object> paramException(ParamException e) {
        logger.error("Parameter verification error，Error status code：【{}】,Cause of error：【{}】", e.getErrorCode(),
            e.getErrorMessage(), e);
        return RestResult.failedWithErrorMessage(e.getErrorCode(), e.getErrorMessage());
    }

    /**
     * MissingServletRequestParameterException
     *
     * @param e
     *            MissingServletRequestParameterException
     * @return Unified response
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RestResult<Object> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("MissingServletRequestParameterException: 【{}】", e.getMessage(), e);
        return RestResult.failedWithErrorMessage(CommonCode.PARAM_CHECK_ERROR.getErrorCode(), e.getMessage());
    }

    /**
     * Request path error
     *
     * @return Unified response
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public RestResult<Object> handlerNoFoundException() {
        return RestResult.failed(CommonCode.ERROR_URL.getErrorCode(), CommonCode.ERROR_URL.getErrorMessage()
            + "，request path:【" + CommonUtil.getServletRequestAttributes().getRequest().getRequestURI() + "】");
    }

    /**
     * Validated bind exception
     *
     * @param e
     *            Exception
     * @return Unified response
     */
    @ExceptionHandler(BindException.class)
    public RestResult<Object> validatedBindException(BindException e) {
        String errorMsg = ArrayUtil
            .join(e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(), ",");
        logger.error("Custom validation exception：【{}】", errorMsg, e);
        return RestResult.failedWithErrorMessage(CommonCode.PARAM_CHECK_ERROR.getErrorCode(), errorMsg);
    }

    /**
     * Custom validation exception
     *
     * @param e
     *            Exception
     * @return Unified response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResult<Object> validExceptionHandler(MethodArgumentNotValidException e) {
        assert e.getBindingResult().getFieldError() != null;
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        logger.error("Custom validation exception：【{}】", message, e);
        return RestResult.failedWithErrorMessage(CommonCode.PARAM_CHECK_ERROR.getErrorCode(), message);
    }

    /**
     * Unsupported request method exception
     *
     * @param e
     *            Exception
     * @return Unified response
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public RestResult<Object> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        logger.error("Unsupported request method：【{}】", e.getMessage(), e);
        return RestResult.failedWithErrorMessage(CommonCode.REQUEST_METHOD_ERROR.getErrorCode(), e.getMessage());
    }

    /**
     * Runtime exception
     *
     * @param e
     *            Exception
     * @return Unified response
     */
    @ExceptionHandler(value = RuntimeException.class)
    public RestResult<Object> runtimeExceptionHandler(RuntimeException e) {
        logger.error("A runtime error occurred on the server，Cause of error：【{}】", e.getMessage(), e);
        return RestResult.failedWithErrorMessage(CommonCode.SERVER_ERROR.getErrorCode(), e.getMessage());
    }

    /**
     * Illegal argument exception
     *
     * @param e
     *            Exception
     * @return Unified response
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public RestResult<Object> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        logger.error("Error during inspection，Cause of error：【{}】", e.getMessage(), e);
        return RestResult.failedWithErrorMessage(CommonCode.ILLEGAL_ARGUMENT_ERROR.getErrorCode(), e.getMessage());
    }

    /**
     * Exception
     *
     * @param e
     *            Exception
     * @return Unified response
     */
    @ExceptionHandler(Exception.class)
    public RestResult<Object> handleException(Exception e) {
        logger.error("Exception：【{}】", e.toString(), e);
        return RestResult.failedWithErrorMessage(CommonCode.SERVER_ERROR.getErrorCode(), e.getMessage());
    }

}

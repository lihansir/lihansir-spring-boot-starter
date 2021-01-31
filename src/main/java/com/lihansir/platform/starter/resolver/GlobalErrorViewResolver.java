/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.resolver;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.lihansir.platform.common.code.CommonCode;
import com.lihansir.platform.common.constant.CommonConstant;
import com.lihansir.platform.common.utils.ServletUtil;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The error page throws an exception directly
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 */
public class GlobalErrorViewResolver implements ErrorViewResolver {
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        ServletUtil.getResponse().setStatus(HttpStatus.OK.value());
        String requestPath = (String)model.getOrDefault("path", "/");
        FastJsonJsonView jsonView = new FastJsonJsonView();
        Map<String, Object> resultModel = new LinkedHashMap<>();
        resultModel.put(CommonConstant.REST_RESPONSE_CODE, CommonCode.ERROR_URL.getCode());
        resultModel.put(CommonConstant.REST_RESPONSE_MESSAGE,
            CommonCode.ERROR_URL.getMsg() + "，request path:【" + requestPath + "】");
        return new ModelAndView(jsonView, resultModel);
    }
}

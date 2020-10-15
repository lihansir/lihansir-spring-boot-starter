/*
 * Copyright Li Han Holding.
 */

package com.lihansir.platform.starter.resolver;

import com.lihansir.platform.common.code.CommonCode;
import com.lihansir.platform.common.exception.BusinessException;
import com.lihansir.platform.common.utils.ServletUtil;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * The error page throws an exception directly
 * </p>
 *
 * @author <a href="https://www.lihansir.com">Li Han</a>
 * @date Created in 2020/10/05 12:34
 */
public class GlobalErrorViewResolver implements ErrorViewResolver {
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        ServletUtil.getResponse().setStatus(HttpStatus.OK.value());
        String requestPath = (String)model.getOrDefault("path", "/");
        throw new BusinessException(CommonCode.ERROR_URL.getCode(),
            CommonCode.ERROR_URL.getMsg() + "，request path:【" + requestPath + "】");
    }
}

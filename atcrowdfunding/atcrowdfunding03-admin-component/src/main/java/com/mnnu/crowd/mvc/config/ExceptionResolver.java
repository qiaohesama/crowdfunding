package com.mnnu.crowd.mvc.config;

import com.google.gson.Gson;
import com.mnnu.crowd.constant.CrowdConstant;
import com.mnnu.crowd.exception.AccessForbiddenException;
import com.mnnu.crowd.exception.LoginAcctInUseException;
import com.mnnu.crowd.exception.LoginFailedException;
import com.mnnu.crowd.util.CrowdUtil;
import com.mnnu.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qiaoh
 */
@ControllerAdvice
public class ExceptionResolver {
    @ExceptionHandler({LoginFailedException.class, AccessForbiddenException.class})
    public String loginFailed(Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String viewName = "admin-login";

        return ajaxResult(viewName, exception, request, response);
    }

    @ExceptionHandler({LoginAcctInUseException.class, Exception.class})
    public String loginAcctInUse(Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String viewName = "system-error";

        return ajaxResult(viewName, exception, request, response);
    }

    private String ajaxResult(String viewName, Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isAjax = CrowdUtil.judgeRequestType(request);
        //先判断是不是异步的请求，如果是的话用resultEntity返回json数据
        if (isAjax) {
            ResultEntity<Object> failed = ResultEntity.failed(exception.getMessage());
            Gson gson = new Gson();
            String json = gson.toJson(failed);
            response.getWriter().println(json);
            return null;
        }
        //不是异步请求返回页面
        request.setAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        return viewName;
    }
}

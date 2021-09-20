package org.mnnu.crowd.filter;

import com.mnnu.crowd.constant.AccessPassResource;
import com.mnnu.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author qiaoh
 */
@Component
public class AccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 1.获取servletContext
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String servletPath = request.getServletPath();


        return !AccessPassResource.judgePass(servletPath);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();
        HttpSession session = request.getSession();

        Object member = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        if (member == null) {
            session.setAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, "用户请先登录");
            try {
                response.sendRedirect("/auth/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }
}

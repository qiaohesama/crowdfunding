package com.mnnu.crowd.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * 供 zuul 使用的判断当前请求资源是否放行的工具类
 * 包含静态资源和路径的集合以及判断方法
 *
 * @author qiaoh
 */
public class AccessPassResource {
    private static final Set<String> PASS_PATH = new HashSet<String>();

    static {
        PASS_PATH.add("/");
        PASS_PATH.add("/auth/to/reg/page");
        PASS_PATH.add("/auth/to/login/page");
        PASS_PATH.add("/auth/reg/member");
        PASS_PATH.add("/auth/member/login");
        PASS_PATH.add("/auth/logout");
    }

    private static final Set<String> PASS_RES = new HashSet<>();

    static {
        PASS_RES.add("bootstrap");
        PASS_RES.add("css");
        PASS_RES.add("fonts");
        PASS_RES.add("img");
        PASS_RES.add("jquery");
        PASS_RES.add("layer");
        PASS_RES.add("script");
        PASS_RES.add("ztree");
    }

    public static boolean judgePass(String servletPath) {
        if (servletPath == null || servletPath.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        if (PASS_PATH.contains(servletPath)) {
            return true;
        }
        String[] words = servletPath.split("/");
        // /aaa 拆分后会是  ["","aaa"] ,所以取下标为 1 的
        String keyWord = words[1];
        return PASS_RES.contains(keyWord);
    }

}

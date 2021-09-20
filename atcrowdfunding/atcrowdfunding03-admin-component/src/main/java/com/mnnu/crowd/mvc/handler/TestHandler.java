package com.mnnu.crowd.mvc.handler;

import com.google.gson.Gson;
import com.mnnu.crowd.entity.Admin;
import com.mnnu.crowd.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author qiaoh
 */
@Controller
public class TestHandler {
    @Autowired
    private AdminMapper adminMapper;

    @RequestMapping("/test")
    public String testSSM(HttpServletResponse response) throws IOException {
        Admin admin = new Admin(1, "11", "qwe", "as", "qq", "sd");
        Gson gson = new Gson();

        response.getWriter().println(gson.toJson(admin));
        return gson.toJson(admin);
    }

    @ResponseBody
    @RequestMapping("/ajax")
    public Admin testAjax() {
        Admin admin = new Admin(1, "11", "qwe", "as", "qq", "sd");
        return admin;
    }

    @ResponseBody
    @RequestMapping("/body")
    public List<?> body(@RequestBody List<Integer> list) {
        return list;
    }
}

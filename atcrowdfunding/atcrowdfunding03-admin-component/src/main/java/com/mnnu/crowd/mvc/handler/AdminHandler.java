package com.mnnu.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.mnnu.crowd.constant.CrowdConstant;
import com.mnnu.crowd.entity.Admin;
import com.mnnu.crowd.exception.LoginAcctInUseException;
import com.mnnu.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author qiaoh
 */
@Controller
@PreAuthorize("hasRole('经理')")
public class AdminHandler {

    private AdminService adminService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * 管理员登录操作,现在已经都交给spring security操作，此handler已经废弃
     *
     * @param loginAcct
     * @param userPswd
     * @param session
     * @return
     */
    @Deprecated
    @PostMapping("/admin/do/login")
    public String doLogin(@RequestParam("loginAcct") String loginAcct,
                          @RequestParam("userPswd") String userPswd,
                          HttpSession session) {

        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);

        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        return "redirect:/admin/to/main/page";
    }

    /**
     * 退出系统，使session失效,现在已经都交给spring security操作，此handler已经废弃
     *
     * @param session 用户的session
     * @return 返回到登陆页面
     */
    @Deprecated
    @GetMapping("/admin/do/logout")
    public String doLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/to/login/page";
    }

    /**
     * 获取分页信息
     *
     * @param keyword  查询的关键词
     * @param pageNum  查询第几页信息
     * @param pageSize 每页包含几个数据
     * @param model    模型
     * @return 返回user页
     */
    @RequestMapping("/admin/get/page")
    public String getPageInfo(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                              Model model) {
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        model.addAttribute(CrowdConstant.ATTR_NAME_PAGEINFO, pageInfo);
        return "user";
    }

    /**
     * 删除指定id的管理员角色
     * 映射两个路径，包含关键字是否为空的两种情况
     * 不可删除自身角色
     *
     * @param id      管理员id
     * @param pageNum 跳转过来时候所在的页数
     * @param keyword 跳转过来时候的关键字
     * @param session 系统的session
     * @return 返回到查询pageInfo
     */
    @RequestMapping({"/admin/delete/{id}/{pageNum}/{keyword}", "/admin/delete/{id}/{pageNum}"})
    public String deleteAdmin(@PathVariable("id") Integer id,
                              @PathVariable("pageNum") Integer pageNum,
                              @PathVariable(value = "keyword", required = false) String keyword,
                              HttpSession session) {
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        if (Objects.equals(admin.getId(), id)) {
            throw new RuntimeException("管理员不可删除自己");
        }
        keyword = (keyword == null ? "" : keyword);
        adminService.removeAdmin(id);
        return "redirect:/admin/get/page?keyword=" + keyword + "&pageNum=" + pageNum;
    }


    /**
     * 添加管理员
     *
     * @param admin
     * @return
     */
    @PreAuthorize("hasAuthority('user:save')")
    @PostMapping("/admin/add/role")
    public String addRole(Admin admin) {
        //密码加密
        admin.setUserPswd(passwordEncoder.encode(admin.getUserPswd()));

        //设置创建时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(date);
        admin.setCreateTime(createTime);


        //保存管理员角色，如果登录账号重复会抛出DuplicateKeyException
        //为了和其他的主键重复异常区分，这里抛出自定义的登录账号重复异常，跳转到controllerAdvice
        try {
            adminService.saveAdmin(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }

        //跳转到最后一页
        return "redirect:/admin/get/page?pageNum=" + Integer.MAX_VALUE;
    }

    /**
     * 携带数据去往更新管理员信息页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("admin/to/update/{id}")
    public String toUpdatePage(@PathVariable("id") Integer id,
                               Model model) {

        Admin adminByPrimaryKey = adminService.getAdminByPrimaryKey(id);
        model.addAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, adminByPrimaryKey);
        return "edit";
    }

    /**
     * 执行更新管理员操作
     *
     * @param admin
     * @param pageNum
     * @return
     */
    @RequestMapping("admin/do/update")
    public String doUpdateSelective(Admin admin, @RequestParam("pageNum") Integer pageNum) {

        adminService.updateAdmin(admin);

        return "redirect:/admin/get/page?pageNum=" + pageNum;
    }

}

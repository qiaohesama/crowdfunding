package com.mnnu.crowd.mvc.handler;

import com.mnnu.crowd.entity.Auth;
import com.mnnu.crowd.entity.Role;
import com.mnnu.crowd.service.api.AdminService;
import com.mnnu.crowd.service.api.AuthService;
import com.mnnu.crowd.service.api.RoleService;
import com.mnnu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author qiaoh
 */
@Controller
public class AssignHandler {
    @Autowired
    RoleService roleService;

    @Autowired
    AdminService adminService;

    @Autowired
    AuthService authService;

    @RequestMapping("/assign/to/page")
    public String toAssignRolePage(@RequestParam("adminId") Integer adminId, Model model) {
        //获取已经被分配的角色
        List<Role> assignedRole = roleService.getAssignedRole(adminId);
        //获取未被分配的角色
        List<Role> unAssignedRole = roleService.getUnAssignedRole(adminId);

        model.addAttribute("assignedRole", assignedRole);
        model.addAttribute("unAssignedRole", unAssignedRole);
        return "assignRole";
    }

    @RequestMapping("/assign/do/assign/role")
    public String assignRole(@RequestParam("adminId") Integer adminId,
                             @RequestParam("pageNum") Integer pageNum,
                             @RequestParam("keyword") String keyword,
                             @RequestParam("roleIdArray") List<Integer> roleIdArray
    ) {
        //删除原先的角色，然后整体再添加
        adminService.updateRoleInfo(adminId, roleIdArray);
        return "redirect:/admin/get/page?pageNum=" + pageNum + "&keyword=" + keyword;
    }


    @ResponseBody
    @RequestMapping("/assign/get/auth/info")
    public ResultEntity<?> getAuthInfo() {
        List<Auth> allAuth = authService.getAllAuth();
        return ResultEntity.successWithData(allAuth);
    }

    @ResponseBody
    @RequestMapping("/assign/get/auth/by/role")
    public ResultEntity<?> getAuthByRole(@RequestParam("roleId") Integer roleId) {
        List<Integer> assignedAuth = roleService.getAssignedAuth(roleId);
        return ResultEntity.successWithData(assignedAuth);
    }

    @ResponseBody
    @RequestMapping("assign/update/auth/for/role")
    public ResultEntity<?> updateRoleAuth(@RequestBody Map<String, List<Integer>> dataMap) {
        roleService.updateRoleAuth(dataMap);
        return ResultEntity.successWithoutData();
    }
}

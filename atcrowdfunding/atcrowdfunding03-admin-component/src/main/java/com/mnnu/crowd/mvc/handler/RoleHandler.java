package com.mnnu.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.mnnu.crowd.entity.Role;
import com.mnnu.crowd.service.api.RoleService;
import com.mnnu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author qiaoh
 */
@Controller
@PreAuthorize("hasRole('部长')")
public class RoleHandler {
    @Autowired
    RoleService roleService;

    @ResponseBody
    @RequestMapping("/role/get/pageInfo")
    public ResultEntity<PageInfo<Role>> getPageInfo(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);

        return ResultEntity.successWithData(pageInfo);
    }

    @ResponseBody
    @RequestMapping("/role/add/newRole")
    public ResultEntity<?> addRole(Role role) {
        roleService.addRole(role);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/update")
    public ResultEntity<?> updateRole(Role role) {
        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }


    /**
     * 单个或者多个删除都是用这个接口
     *
     * @param roles
     * @return
     */
    @ResponseBody
    @RequestMapping("/role/delete")
    public ResultEntity<?> deleteRole(@RequestBody List<Role> roles) {
        roleService.deleteRole(roles);
        return ResultEntity.successWithoutData();
    }
}

package com.mnnu.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.mnnu.crowd.entity.Auth;
import com.mnnu.crowd.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * @author qiaoh
 */
public interface RoleService {

    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    void addRole(Role role);

    void updateRole(Role role);

    void deleteRole(List<Role> roles);

    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnAssignedRole(Integer adminId);

    List<Integer> getAssignedAuth(Integer roleId);

    void updateRoleAuth(Map<String, List<Integer>> dataMap);
}

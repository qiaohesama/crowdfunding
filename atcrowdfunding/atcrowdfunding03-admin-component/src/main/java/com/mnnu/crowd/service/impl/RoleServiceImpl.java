package com.mnnu.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnnu.crowd.entity.Role;
import com.mnnu.crowd.entity.RoleExample;
import com.mnnu.crowd.mapper.RoleMapper;
import com.mnnu.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qiaoh
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {

        PageHelper.startPage(pageNum, pageSize);

        List<Role> roles = roleMapper.selectByKeyword(keyword);

        return new PageInfo<>(roles);

    }

    @Override
    public void addRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }


    /**
     * 用 QBC查询 删除角色
     *
     * @param roles
     */
    @Override
    public void deleteRole(List<Role> roles) {
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();

        List<Integer> list = roles.stream().map(Role::getId).collect(Collectors.toList());

        criteria.andIdIn(list);

        roleMapper.deleteByExample(example);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {
        return roleMapper.selectAssigned(adminId);
    }

    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        return roleMapper.selectUnAssigned(adminId);
    }

    @Override
    public List<Integer> getAssignedAuth(Integer roleId) {
        return roleMapper.selectAssignedAuth(roleId);
    }

    @Override
    public void updateRoleAuth(Map<String, List<Integer>> dataMap) {
        //先删除原先属于该角色的权限信息
        Integer roleId = dataMap.get("roleId").get(0);
        roleMapper.deleteAuthInfo(roleId);
        //插入新的权限信息
        List<Integer> authIdArr = dataMap.get("authIdArr");
        if (authIdArr != null && authIdArr.size() > 0) {

            roleMapper.insertAuthInfo(roleId, authIdArr);
        }
    }
}

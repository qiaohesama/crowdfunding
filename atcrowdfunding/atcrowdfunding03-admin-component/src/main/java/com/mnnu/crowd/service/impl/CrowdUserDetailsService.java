package com.mnnu.crowd.service.impl;

import com.mnnu.crowd.constant.CrowdConstant;
import com.mnnu.crowd.entity.Admin;
import com.mnnu.crowd.entity.AdminExample;
import com.mnnu.crowd.exception.LoginFailedException;
import com.mnnu.crowd.mapper.AdminMapper;
import com.mnnu.crowd.mapper.AuthMapper;
import com.mnnu.crowd.mapper.RoleMapper;
import com.mnnu.crowd.mvc.config.SecurityAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiaoh
 */
@Component
public class CrowdUserDetailsService implements UserDetailsService {

    final
    RoleMapper roleMapper;

    final
    AuthMapper authMapper;

    final
    AdminMapper adminMapper;

    public CrowdUserDetailsService(RoleMapper roleMapper, AuthMapper authMapper, AdminMapper adminMapper) {
        this.roleMapper = roleMapper;
        this.authMapper = authMapper;
        this.adminMapper = adminMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 1.先查出admin
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(s);
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        if (admins == null || admins.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        Admin admin = admins.get(0);
        // 2.查出已分配的角色
        List<String> roleNames = roleMapper
                .selectAssigned(admin.getId())
                .stream()
                .map((role) -> "ROLE_" + role.getName())
                .collect(Collectors.toList());

        // 3.查出分配给角色的权限
        List<String> authNames = authMapper.selectAuthByAdminId(admin.getId());
        // 4.封装到securityAdmin对象中
        List<GrantedAuthority> authorities = new ArrayList<>(20);
        for (String name :
                roleNames) {
            authorities.add(new SimpleGrantedAuthority(name));
        }
        for (String authName : authNames) {
            authorities.add(new SimpleGrantedAuthority(authName));
        }
        return new SecurityAdmin(admin, authorities);
    }
}

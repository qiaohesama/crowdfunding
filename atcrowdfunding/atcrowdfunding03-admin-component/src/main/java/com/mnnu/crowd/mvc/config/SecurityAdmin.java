package com.mnnu.crowd.mvc.config;

import com.mnnu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 对security的user对象进行扩展
 *
 * @author qiaoh
 */
public class SecurityAdmin extends User {
    /**
     * 原始admin对象
     */
    Admin admin;

    public SecurityAdmin(Admin admin, Collection<? extends GrantedAuthority> authorities) {
        super(admin.getUserName(), admin.getUserPswd(), authorities);
        this.admin = admin;
        admin.setUserPswd("");
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}

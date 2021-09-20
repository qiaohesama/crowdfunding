package com.mnnu.crowd;


import com.mnnu.crowd.entity.Admin;
import com.mnnu.crowd.entity.Role;
import com.mnnu.crowd.mapper.AdminMapper;
import com.mnnu.crowd.mapper.RoleMapper;
import com.mnnu.crowd.service.api.AdminService;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-persist-mybatis.xml", "classpath:spring-persist-tx.xml"})
public class Test {


    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    AdminService adminService;

    @Autowired
    RoleMapper roleMapper;

    @org.junit.Test
    public void testInsert() {
        for (int i = 2; i <= 237; i++) {
            Admin admin = new Admin(null, "loginAcct-" + i, "123123", "userName-" + i, "email-" + i, null);
            adminMapper.insert(admin);
        }
    }

    @org.junit.Test
    public void testRole() {
        for (int i = 1; i <= 237; i++) {
            Role role = new Role(null, "role-" + i);
            roleMapper.insert(role);
        }
    }

    @org.junit.Test
    public void testConn() throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-persist-mybatis.xml");
        Object datasource = context.getBean("datasource");
        DataSource source = (DataSource) datasource;
        System.out.println(source.getConnection());
    }

    @org.junit.Test
    public void testTX() {
        Admin admin = new Admin(null, "jerry",
                "123123", "杰瑞", "jerry@qq.com", null);
        adminService.saveAdmin(admin);
    }
}

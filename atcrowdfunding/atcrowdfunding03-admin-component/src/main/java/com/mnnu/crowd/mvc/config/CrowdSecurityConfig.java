package com.mnnu.crowd.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 注解的权限控制要开启
 *
 * @author qiaoh
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CrowdSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    /**
     * 调用该方法ioc会先看容器是否有该组件
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/admin/to/login/page")
                .permitAll()
                .antMatchers("/bootstrap/**", "/css/**", "/fonts/**", "/img/**", "/jquery/**", "/layer/**", "/script/**", "/ztree/**", "/js/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()                          //关闭csrf功能
                .csrf()
                .disable()
                .formLogin()                    //开启表单登录
                .loginPage("/admin/to/login/page")
                .loginProcessingUrl("/security/do/login")
                .defaultSuccessUrl("/admin/to/main/page")
                .usernameParameter("loginAcct").passwordParameter("userPswd")
                .and()
                .logout()
                .logoutUrl("/security/do/logout")
                .logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getBCryptPasswordEncoder());
    }
}

package org.mnnu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author qiaoh
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry
                .addViewController("/auth/to/reg/page")
                .setViewName("register");
        registry.addViewController("/auth/to/login/page")
                .setViewName("login");
        registry.addViewController("/auth/to/member/page")
                .setViewName("member");
        registry.addViewController("/auth/my/crowd")
                .setViewName("minecrowdfunding");
    }
}

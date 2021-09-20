package org.mnnu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启用feign的功能
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class Atcrowdfunding12MemberAuthenticationConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Atcrowdfunding12MemberAuthenticationConsumerApplication.class, args);
    }

}

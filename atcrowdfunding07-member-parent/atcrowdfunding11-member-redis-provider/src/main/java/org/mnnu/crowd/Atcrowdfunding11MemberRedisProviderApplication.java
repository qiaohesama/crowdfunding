package org.mnnu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@EnableEurekaClient
@SpringBootApplication
public class Atcrowdfunding11MemberRedisProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(Atcrowdfunding11MemberRedisProviderApplication.class, args);
    }

}

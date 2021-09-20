package org.mnnu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class Atcrowdfunding10MemberMysqlProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(Atcrowdfunding10MemberMysqlProviderApplication.class, args);
    }

}

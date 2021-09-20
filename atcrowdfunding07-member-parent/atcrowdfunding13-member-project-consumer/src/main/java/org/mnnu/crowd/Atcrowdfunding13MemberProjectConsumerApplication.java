package org.mnnu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Atcrowdfunding13MemberProjectConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Atcrowdfunding13MemberProjectConsumerApplication.class, args);
    }

}

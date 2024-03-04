package com.enexse.intranet.ms.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}/*scanBasePackages={"com.enexse.intranet.ms.users.repositories"}*/)
@EnableEurekaClient
@EnableFeignClients
public class EesMsUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(EesMsUsersApplication.class, args);
    }

}

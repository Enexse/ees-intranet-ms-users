package com.enexse.intranet.ms.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}/*scanBasePackages={"com.enexse.intranet.ms.users.repositories"}*/)
@EnableEurekaClient
@EnableFeignClients
@Slf4j
public class EesMsUsersApplication {

    @Value("${eureka.client.serviceUrl.defaultZone}")
    private static String defaultZone;

    public static void main(String[] args) {
        log.info("Environment defaultZone: " + defaultZone);
        SpringApplication.run(EesMsUsersApplication.class, args);
    }

}

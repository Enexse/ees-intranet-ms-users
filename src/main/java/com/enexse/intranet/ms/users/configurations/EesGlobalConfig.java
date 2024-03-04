package com.enexse.intranet.ms.users.configurations;

import com.enexse.intranet.ms.users.models.partials.EesGenerator;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class EesGlobalConfig {
    @Bean
    public EesGenerator myGenerator() {
        return new EesGenerator();
    }

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public EesCommonUtil eesCommonUtil() {
        return new EesCommonUtil();
    }

    /*@Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        // Set the maximum file size allowed
        multipartResolver.setMaxUploadSize(10 * 1024 * 1024); // 10MB
        return multipartResolver;
    }*/
}

package com.enexse.intranet.ms.users.configurations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EesCloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    public String cloudName;

    @Value("${cloudinary.api-key}")
    public String apikey;

    @Value("${cloudinary.secret-key}")
    public String secretKey;


    @Bean
    public Cloudinary cloudinaryBean() {
        // Set Cloudinary instance
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName, // insert here you cloud name
                "api_key", apikey, // insert here your api code
                "api_secret", secretKey)); // insert here your api secret
        return cloudinary;
    }
}

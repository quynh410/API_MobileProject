package com.ra.api_project.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dv0gntcir",
                "api_key", "882568732183815",
                "api_secret", "e1Zp9d16zqmm1HPyfk0CR9dZFv8",
                "secure", true
        ));
    }
}


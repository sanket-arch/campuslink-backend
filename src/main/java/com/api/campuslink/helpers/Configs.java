package com.api.campuslink.helpers;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Objects;

@Configuration
public class Configs {

    @Autowired
    Environment environment;

    @Bean
    public Cloudinary cloudinaryConfig() {
        Map cloudinaryConfig = Map.of(
                "cloud_name", Objects.requireNonNull(environment.getProperty("CLOUDINARY_CLOUD_NAME")),
                "api_key", Objects.requireNonNull(environment.getProperty("CLOUDINARY_API_KEY")),
                "api_secret", Objects.requireNonNull(environment.getProperty("CLOUDINARY_API_SECRET")),
                "secure", true);

        return new Cloudinary(cloudinaryConfig);
    }

}

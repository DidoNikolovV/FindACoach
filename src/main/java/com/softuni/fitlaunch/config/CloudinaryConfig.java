package com.softuni.fitlaunch.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${app.cloudinary.api-secret}")
    private String CLOUD_NAME;

    @Value("${app.cloudinary.api-key}")
    private String API_KEY;

    @Value("${app.cloudinary.cloud-name}")
    private String API_SECRET;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> cfg = new HashMap<>();
        cfg.put("cloud_name", CLOUD_NAME);
        cfg.put("api_key", API_KEY);
        cfg.put("api_secret", API_SECRET);

        return new Cloudinary(cfg);
    }

}

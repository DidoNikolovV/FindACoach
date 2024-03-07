package com.softuni.fitlaunch.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    private final String CLOUD_NAME = "diffdysmk";
    private final String API_KEY = "819549632415934";
    private final String API_SECRET = "MlXIgJA3bqnfjVfD2bCzGKXlm00";

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> cfg = new HashMap<>();
        cfg.put("cloud_name", CLOUD_NAME);
        cfg.put("api_key", API_KEY);
        cfg.put("api_secret", API_SECRET);

        return new Cloudinary(cfg);
    }

}

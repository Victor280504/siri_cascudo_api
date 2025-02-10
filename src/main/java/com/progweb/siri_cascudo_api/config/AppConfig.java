package com.progweb.siri_cascudo_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.upload-dir}")
    private String uploadDir;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUploadDir() {
        return uploadDir;
    }
}
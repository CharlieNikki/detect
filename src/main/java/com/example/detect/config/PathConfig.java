package com.example.detect.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathConfig {

    @Value("${path.save-image-path}")
    private String saveImagePath;

    @Value("${path.url-prefix}")
    private String urlPrefix;


}

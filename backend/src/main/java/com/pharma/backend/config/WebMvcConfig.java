package com.pharma.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import com.pharma.backend.security.RateLimitingInterceptor;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Resolve 'uploads' relative to the working directory (where mvn spring-boot:run is run from)
        // When run from pharma/pharma/backend, this becomes pharma/pharma/backend/uploads/
        Path uploadsDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        String uploadsAbsPath = uploadsDir.toString().replace("\\", "/");
        if (!uploadsAbsPath.endsWith("/")) uploadsAbsPath += "/";

        System.out.println("[WebMvcConfig] Serving /uploads/** from: " + uploadsAbsPath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + uploadsAbsPath);
    }

    @Autowired
    private RateLimitingInterceptor rateLimitingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor)
                .addPathPatterns("/api/**");
    }
}

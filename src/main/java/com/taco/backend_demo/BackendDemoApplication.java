package com.taco.backend_demo;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendDemoApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
//
//    访问 API 文档：应用启动后，您可以通过以下地址访问生成的 API 文档：
//
//    Swagger UI：http://localhost:8080/swagger-ui.html
//    OpenAPI 规范：http://localhost:8080/v3/api-docs
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("后端演示 API")
                        .version("1.0.0")
                        .description("后端演示项目的 API 文档"));
    }

}
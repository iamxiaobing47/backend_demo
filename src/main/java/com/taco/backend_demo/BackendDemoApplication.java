package com.taco.backend_demo;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 1. Spring Boot应用主类：启动后端演示应用程序
 * 2. MyBatis Mapper扫描：自动扫描指定包下的Mapper接口
 * 3. OpenAPI文档配置：集成Swagger UI生成API文档
 */
@SpringBootApplication
@MapperScan("com.taco.backend_demo.mapper")
public class BackendDemoApplication {

    /**
     * 1. 应用程序入口点：启动Spring Boot应用
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendDemoApplication.class, args);
    }

    /**
     * 2. OpenAPI文档配置：定义API文档的基本信息
     * @return OpenAPI配置对象
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("后端演示 API")
                        .version("1.0.0")
                        .description("后端演示项目的 API 文档"));
    }
}

package com.taco.backend_demo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置类
 * 注意：分页功能在 Controller 中手动处理 SQL（PostgreSQL 兼容的 LIMIT/OFFSET 语法）
 */
@Configuration
public class MybatisPlusConfig {
    /**
     * 注册 MyBatis-Plus 拦截器，并添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        // 1. 创建主拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 2. ‼️ 关键：添加分页内部拦截器 ‼️
        //    并指定你的数据库类型（我的是 POSTGRE_SQL）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));

        return interceptor;
    }
}

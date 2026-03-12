package com.taco.backend_demo.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询用户请求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageUserQueryRequest {

    /**
     * 页码（从 1 开始）
     */
    @NotNull(message = "E014")
    @Min(value = 1, message = "E014")
    private Integer pageNum;

    /**
     * 每页数量
     */
    @NotNull(message = "E014")
    @Min(value = 1, message = "E014")
    private Integer pageSize;

    /**
     * 用户类型过滤（可选）
     */
    private String userType;

    /**
     * 邮箱模糊查询（可选）
     */
    private String email;

    /**
     * 用户名模糊查询（可选）
     */
    private String userName;
}

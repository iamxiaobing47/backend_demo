package com.taco.backend_demo.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户信息 DTO
 * 用于前后端数据传输的用户信息模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    /**
     * 用户主键 pk
     */
    private Long pk;

    /**
     * 用户类型：BUSINESS_USER 或 STAFF_USER
     */
    private String userType;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 组织 ID（业务 pk 或位置 pk）
     */
    private String orgId;
}

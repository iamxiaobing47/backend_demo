package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("password")
public class PasswordEntity extends BaseEntity {
    private String email;
    private String password;
    private Boolean isLocked;
    private String loginStatus;
    private Integer retryCount;
}

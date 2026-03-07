package com.taco.backend_demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class TokenEntity extends BaseEntity {
    private String email;
    private String token;
    private LocalDateTime expiresAt;
}

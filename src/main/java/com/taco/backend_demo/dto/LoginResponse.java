package com.taco.backend_demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String nickname;
    private Long userId;
    private String avatar;
    private Long expiresIn;
}

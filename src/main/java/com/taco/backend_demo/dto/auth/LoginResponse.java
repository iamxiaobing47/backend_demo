package com.taco.backend_demo.dto.auth;

import com.taco.backend_demo.dto.user.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private UserInfo userInfo;
}

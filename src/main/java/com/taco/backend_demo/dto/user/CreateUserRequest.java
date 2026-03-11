package com.taco.backend_demo.dto.user;

import com.taco.backend_demo.validation.PasswordStrength;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "E014")
    @Email(message = "E015")
    private String email;
    
    @NotBlank(message = "E014")
    @PasswordStrength(value = PasswordStrength.StrengthLevel.SIMPLE, message = "E016")
    private String password;

    private String userId;
    private String userType;
    private String userName;
    private String orgId;
}
package com.taco.backend_demo.dto.password;

import com.taco.backend_demo.validation.PasswordStrength;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "E014")
    private String currentPassword;
    
    @NotBlank(message = "E014")
    @PasswordStrength(value = PasswordStrength.StrengthLevel.SIMPLE, message = "E016")
    private String newPassword;
}
package com.taco.backend_demo.dto.auth;

import com.taco.backend_demo.validation.PasswordStrength;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = E014)
    @Email(message = E015)
    private String email;

    @NotBlank(message = E014)
    @PasswordStrength(value = PasswordStrength.StrengthLevel.SIMPLE, message = E016)
    private String password;
}

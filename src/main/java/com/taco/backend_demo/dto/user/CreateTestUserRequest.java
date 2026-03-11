package com.taco.backend_demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.taco.backend_demo.validation.PasswordStrength;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTestUserRequest {
    @NotBlank(message = "E014")
    @Email(message = "E015")
    private String email;
    
    @NotBlank(message = "E014")
    @PasswordStrength(value = PasswordStrength.StrengthLevel.SIMPLE, message = "E016")
    private String password;
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
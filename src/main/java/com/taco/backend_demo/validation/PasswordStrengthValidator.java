package com.taco.backend_demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {
    
    private PasswordStrength.StrengthLevel level;
    
    @Override
    public void initialize(PasswordStrength constraintAnnotation) {
        this.level = constraintAnnotation.value();
    }
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return true; // 空值由@NotBlank处理
        }
        
        switch (level) {
            case SIMPLE:
                return password.length() >= 3;
            case MEDIUM:
                return password.length() >= 8 && 
                       Pattern.compile("[a-z]").matcher(password).find() && 
                       Pattern.compile("[A-Z]").matcher(password).find();
            case STRONG:
                return password.length() >= 8 && 
                       Pattern.compile("[a-z]").matcher(password).find() && 
                       Pattern.compile("[A-Z]").matcher(password).find() && 
                       Pattern.compile("\\d").matcher(password).find();
            case VERY_STRONG:
                return password.length() >= 10 && 
                       Pattern.compile("[a-z]").matcher(password).find() && 
                       Pattern.compile("[A-Z]").matcher(password).find() && 
                       Pattern.compile("\\d").matcher(password).find() && 
                       Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(password).find();
            default:
                return true;
        }
    }
}
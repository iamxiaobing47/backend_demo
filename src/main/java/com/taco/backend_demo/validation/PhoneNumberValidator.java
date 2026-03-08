package com.taco.backend_demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    
    private String[] countryCodes;
    
    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        this.countryCodes = constraintAnnotation.countryCode();
    }
    
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) {
            return true; // 空值由@NotBlank处理
        }
        
        for (String countryCode : countryCodes) {
            switch (countryCode) {
                case "CN":
                    // 中国手机号格式：11位数字，以1开头
                    if (Pattern.compile("^1[3-9]\\d{9}$").matcher(phone).matches()) {
                        return true;
                    }
                    break;
                case "US":
                    // 美国手机号格式：10位数字，可选的国家代码+1
                    if (Pattern.compile("^(\\+1)?[2-9]\\d{9}$").matcher(phone).matches()) {
                        return true;
                    }
                    break;
                default:
                    // 默认接受任何非空字符串（由其他验证处理）
                    return true;
            }
        }
        return false;
    }
}
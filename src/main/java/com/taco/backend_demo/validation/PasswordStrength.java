package com.taco.backend_demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordStrengthValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface PasswordStrength {
    String message() default "E016";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    // 密码强度级别
    StrengthLevel value() default StrengthLevel.MEDIUM;
    
    enum StrengthLevel {
        SIMPLE,      // 简单：仅长度要求
        MEDIUM,      // 中等：长度 + 大小写字母
        STRONG,      // 强：长度 + 大小写字母 + 数字
        VERY_STRONG  // 非常强：长度 + 大小写字母 + 数字 + 特殊字符
    }
}
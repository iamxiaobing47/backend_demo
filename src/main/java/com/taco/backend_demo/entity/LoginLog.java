package com.taco.backend_demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginLog {
    private Long id;
    private String username;
    private String ip;
    private String userAgent;
    private String status;
    private String message;
    private LocalDateTime loginTime;
}

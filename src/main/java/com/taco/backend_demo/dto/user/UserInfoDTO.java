package com.taco.backend_demo.dto.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserInfoDTO {
    private Long pk;
    private Long userId;
    private String userType;
    private String email;
    private String userName;
    private Long orgId;
    private String orgName;
    private String orgType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

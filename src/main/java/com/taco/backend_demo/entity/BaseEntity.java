package com.taco.backend_demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public abstract class BaseEntity {
    private Long pk;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

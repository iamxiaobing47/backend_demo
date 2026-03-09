package com.taco.backend_demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class NavigationDto {
    private Long navigationId;
    private String name;
    private String path;
    private String icon;
    private Integer sortOrder;
    private Long parentId;
    private Boolean enabled;
    private String userType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<NavigationDto> children;
}
package com.taco.backend_demo.dto.navigation;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NavigationDTO {
    private Long pk;
    private String name;
    private String path;
    private String icon;
    private Integer sortOrder;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<NavigationDTO> children;
}

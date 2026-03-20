package com.taco.backend_demo.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * 地域 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {

    private Integer regionCd;

    @NotBlank(message = "地域名は必須です")
    private String regionNm;
}

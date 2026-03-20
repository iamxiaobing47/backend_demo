package com.taco.backend_demo.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地域 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {

    private Integer regionCd;
    private String regionNm;
}

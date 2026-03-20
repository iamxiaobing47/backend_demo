package com.taco.backend_demo.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 国家 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {

    private Integer countryCd;
    private Integer regionCd;
    private String countryNm;
}

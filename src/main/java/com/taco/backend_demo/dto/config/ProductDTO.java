package com.taco.backend_demo.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 品目 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Integer productCd;
    private String productNm;
}

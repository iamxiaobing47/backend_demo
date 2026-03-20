package com.taco.backend_demo.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * 品目 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Integer productCd;

    @NotBlank(message = "品目名は必須です")
    private String productNm;
}

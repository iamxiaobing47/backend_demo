package com.taco.backend_demo.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 国家 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KuniDTO {

    private Integer kuniCd;

    @NotNull(message = "地域コードは必須です")
    private Integer chiikiCd;

    @NotBlank(message = "国名は必須です")
    private String kuniNm;
}

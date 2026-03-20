package com.taco.backend_demo.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 申请书模板 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationTemplateDTO {

    private Integer templateId;

    @NotNull(message = "地域コードは必須です")
    private Integer regionCd;

    @NotNull(message = "国コードは必須です")
    private Integer countryCd;

    @NotNull(message = "品目コードは必須です")
    private Integer productCd;

    @NotBlank(message = "テンプレート名は必須です")
    private String templateNm;

    @NotBlank(message = "ファイルパスは必須です")
    private String filePath;
}

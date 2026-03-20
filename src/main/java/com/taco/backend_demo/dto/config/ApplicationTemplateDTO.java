package com.taco.backend_demo.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 申請書テンプレート DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationTemplateDTO {

    private Integer templateId;
    private Integer regionCd;
    private Integer countryCd;
    private Integer productCd;
    private String templateNm;
    private String filePath;
}

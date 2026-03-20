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
public class YoushikiDTO {

    private Integer youshikiId;

    @NotNull(message = "国コードは必須です")
    private Integer kuniCd;

    @NotNull(message = "品目コードは必須です")
    private Integer hinmokuCd;

    @NotBlank(message = "様式名は必須です")
    private String youshikiNm;

    @NotBlank(message = "ファイルパスは必須です")
    private String filePath;
}

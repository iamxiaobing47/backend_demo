package com.taco.backend_demo.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下拉选项 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionItem {

    /**
     * 选项值
     */
    private String value;

    /**
     * 选项标题
     */
    private String title;
}

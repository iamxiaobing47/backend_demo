package com.taco.backend_demo.entity.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 申请书模板实体类
 * 对应数据库表：application_template
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("application_template")
public class ApplicationTemplateEntity {

    /**
     * 模板 ID(主键，自增)
     */
    @TableId(value = "pk", type = IdType.AUTO)
    private Integer templateId;

    /**
     * 地域代码
     */
    @TableField("region_cd")
    private Integer regionCd;

    /**
     * 国代码
     */
    @TableField("country_cd")
    private Integer countryCd;

    /**
     * 品目代码
     */
    @TableField("product_cd")
    private Integer productCd;

    /**
     * 模板名称
     */
    @TableField("template_nm")
    private String templateNm;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}

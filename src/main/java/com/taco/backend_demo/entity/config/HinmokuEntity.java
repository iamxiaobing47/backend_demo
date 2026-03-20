package com.taco.backend_demo.entity.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 品目实体类
 * 对应数据库表：c_hinmoku
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("c_hinmoku")
public class HinmokuEntity {

    /**
     * 品目代码 (主键，自增)
     */
    @TableId(value = "hinmoku_cd", type = IdType.AUTO)
    private Integer hinmokuCd;

    /**
     * 品目名
     */
    @TableField("hinmoku_nm")
    private String hinmokuNm;

    /**
     * 品目英文名
     */
    @TableField("hinmoku_en")
    private String hinmokuEn;

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

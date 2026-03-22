package com.taco.backend_demo.entity.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 地域实体类
 * 对应数据库表：region
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("region")
public class RegionEntity {

    /**
     * 主键 (自增)
     */
    @TableId(value = "pk", type = IdType.AUTO)
    private Integer pk;

    /**
     * 地域代码
     */
    @TableField("region_cd")
    private Integer regionCd;

    /**
     * 地域名
     */
    @TableField("region_nm")
    private String regionNm;

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

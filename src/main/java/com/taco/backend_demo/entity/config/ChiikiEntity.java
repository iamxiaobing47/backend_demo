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
 * 对应数据库表：c_yushutsusaki_chiiki
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("c_yushutsusaki_chiiki")
public class ChiikiEntity {

    /**
     * 地域代码 (主键，自增)
     */
    @TableId(value = "chiiki_cd", type = IdType.AUTO)
    private Integer chiikiCd;

    /**
     * 地域名
     */
    @TableField("chiiki_nm")
    private String chiikiNm;

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

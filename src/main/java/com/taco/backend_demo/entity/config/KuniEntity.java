package com.taco.backend_demo.entity.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 国家实体类
 * 对应数据库表：c_kuni
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("c_kuni")
public class KuniEntity {

    /**
     * 国代码 (主键，自增)
     */
    @TableId(value = "kuni_cd", type = IdType.AUTO)
    private Integer kuniCd;

    /**
     * 所属地域代码
     */
    @TableField("chiiki_cd")
    private Integer chiikiCd;

    /**
     * 国名
     */
    @TableField("kuni_nm")
    private String kuniNm;

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

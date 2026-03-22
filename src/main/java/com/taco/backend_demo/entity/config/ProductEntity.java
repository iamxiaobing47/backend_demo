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
 * 对应数据库表：product
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product")
public class ProductEntity {

    /**
     * 品目代码 (主键，自增)
     */
    @TableId(value = "pk", type = IdType.AUTO)
    private Integer productCd;

    /**
     * 品目名
     */
    @TableField("product_nm")
    private String productNm;

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

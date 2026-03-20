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
 * 对应数据库表：p_ikkatsu_youshiki
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("p_ikkatsu_youshiki")
public class YoushikiEntity {

    /**
     * 样式 ID(主键，自增)
     */
    @TableId(value = "youshiki_id", type = IdType.AUTO)
    private Integer youshikiId;

    /**
     * 国代码
     */
    @TableField("kuni_cd")
    private Integer kuniCd;

    /**
     * 品目代码
     */
    @TableField("hinmoku_cd")
    private Integer hinmokuCd;

    /**
     * 样式名称
     */
    @TableField("youshiki_nm")
    private String youshikiNm;

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

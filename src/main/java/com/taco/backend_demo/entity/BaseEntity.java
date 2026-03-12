package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 1. 基础实体类：所有数据库实体的公共基类
 * 2. 主键管理：使用自增主键（pk字段）
 * 3. 时间戳：自动维护创建时间和更新时间
 */
public abstract class BaseEntity {
    /**
     * 1. 主键字段：数据库自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long pk;
    
    /**
     * 2. 创建时间：记录实体创建的时间戳
     */
    private LocalDateTime createdAt;
    
    /**
     * 3. 更新时间：记录实体最后更新的时间戳
     */
    private LocalDateTime updatedAt;
    
    /**
     * 1. 获取主键值
     * @return 主键值
     */
    public Long getPk() {
        return pk;
    }
    
    /**
     * 2. 设置主键值
     * @param pk 主键值
     */
    public void setPk(Long pk) {
        this.pk = pk;
    }
    
    /**
     * 3. 获取创建时间
     * @return 创建时间戳
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * 4. 设置创建时间
     * @param createdAt 创建时间戳
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * 5. 获取更新时间
     * @return 更新时间戳
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * 6. 设置更新时间
     * @param updatedAt 更新时间戳
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

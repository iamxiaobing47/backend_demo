package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public abstract class BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long pk;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Long getPk() {
        return pk;
    }
    
    public void setPk(Long pk) {
        this.pk = pk;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
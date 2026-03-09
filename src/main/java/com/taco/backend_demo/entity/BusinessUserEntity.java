package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("business_users")
public class BusinessUserEntity extends BaseEntity {
    private Long businessUserId;
    private Long businessId;
    private String email;
    private String name;
    private String position;
    
    public Long getBusinessUserId() {
        return businessUserId;
    }
    
    public void setBusinessUserId(Long businessUserId) {
        this.businessUserId = businessUserId;
    }
    
    public Long getBusinessId() {
        return businessId;
    }
    
    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
}
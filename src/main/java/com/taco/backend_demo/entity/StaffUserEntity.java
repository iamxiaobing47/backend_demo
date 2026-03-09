package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("staff_users")
public class StaffUserEntity extends BaseEntity {
    private Long staffUserId;
    private Long locationId;
    private String email;
    private String name;
    private String position;
    
    public Long getStaffUserId() {
        return staffUserId;
    }
    
    public void setStaffUserId(Long staffUserId) {
        this.staffUserId = staffUserId;
    }
    
    public Long getLocationId() {
        return locationId;
    }
    
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
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
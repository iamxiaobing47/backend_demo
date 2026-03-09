package com.taco.backend_demo.dto;

public class UserInfoDto {
    private String email;
    private String username;
    private String role;
    private String businessOwnerId;
    private String locationId;
    
    // 无参构造函数
    public UserInfoDto() {}
    
    // 全参构造函数
    public UserInfoDto(String email, String username, String role, String businessOwnerId, String locationId) {
        this.email = email;
        this.username = username;
        this.role = role;
        this.businessOwnerId = businessOwnerId;
        this.locationId = locationId;
    }
    
    // getter和setter方法
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getBusinessOwnerId() {
        return businessOwnerId;
    }
    
    public void setBusinessOwnerId(String businessOwnerId) {
        this.businessOwnerId = businessOwnerId;
    }
    
    public String getLocationId() {
        return locationId;
    }
    
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
package com.taco.backend_demo.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taco.backend_demo.common.constants.LoginConstants;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.entity.BusinessUserEntity;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.StaffUserEntity;
import com.taco.backend_demo.mapper.mp.BusinessUserMapper;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.StaffUserMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E001;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordMapper passwordMapper;
    
    @Autowired
    private BusinessUserMapper businessUserMapper;
    
    @Autowired
    private StaffUserMapper staffUserMapper;

    @Override
    public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {

        LambdaQueryWrapper<PasswordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PasswordEntity::getEmail, username);

        PasswordEntity passwordEntity = passwordMapper.selectOne(wrapper);

        if (passwordEntity == null) {
            throw new BusinessException(E001);
        }

        // Determine user role and association by checking business_users and staff_users tables
        String role = null;
        String businessOwnerId = null;
        String locationId = null;
        
        // Check if user is a business owner
        LambdaQueryWrapper<BusinessUserEntity> businessUserWrapper = new LambdaQueryWrapper<>();
        businessUserWrapper.eq(BusinessUserEntity::getEmail, username);
        BusinessUserEntity businessUser = businessUserMapper.selectOne(businessUserWrapper);
        
        if (businessUser != null) {
            role = "business_owner";
            businessOwnerId = businessUser.getBusinessId().toString(); // Convert to string
        } else {
            // Check if user is a staff member
            LambdaQueryWrapper<StaffUserEntity> staffUserWrapper = new LambdaQueryWrapper<>();
            staffUserWrapper.eq(StaffUserEntity::getEmail, username);
            StaffUserEntity staffUser = staffUserMapper.selectOne(staffUserWrapper);
            
            if (staffUser != null) {
                role = "employee";
                locationId = staffUser.getLocationId().toString(); // Convert to string
            } else {
                // Default to employee role if not found in either table
                role = "employee";
            }
        }

        return new LoginUser(passwordEntity, Collections.singletonList(LoginConstants.ROLE_GUEST), 
                           role, businessOwnerId, locationId);
    }

    public void updateLastLogin(String email) {
        LambdaQueryWrapper<PasswordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PasswordEntity::getEmail, email);
        
        PasswordEntity updateEntity = new PasswordEntity();
        updateEntity.setLastLoginAt(LocalDateTime.now());
        passwordMapper.update(updateEntity, wrapper);
    }

    public void resetRetryCount(String email) {
        LambdaQueryWrapper<PasswordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PasswordEntity::getEmail, email);
        
        PasswordEntity updateEntity = new PasswordEntity();
        updateEntity.setRetryCount(0);
        updateEntity.setIsLocked(false);
        passwordMapper.update(updateEntity, wrapper);
    }
}

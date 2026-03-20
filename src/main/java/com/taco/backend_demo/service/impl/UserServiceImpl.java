package com.taco.backend_demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.dto.user.PageUserQueryRequest;
import com.taco.backend_demo.entity.BusinessUserEntity;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.StaffUserEntity;
import com.taco.backend_demo.entity.UserInfoEntity;
import com.taco.backend_demo.mapper.mp.BusinessUserMapper;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.StaffUserMapper;
import com.taco.backend_demo.mapper.mp.VUserInfoMapper;
import com.taco.backend_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E008;
import static com.taco.backend_demo.common.message.ErrorMessageCodes.E010;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<VUserInfoMapper, UserInfoEntity> implements UserService {

    @Autowired
    private PasswordMapper passwordMapper;

    @Autowired
    private StaffUserMapper staffUserMapper;

    @Autowired
    private BusinessUserMapper businessUserMapper;

    @Autowired
    private VUserInfoMapper vUserInfoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createUser(String email, String password, String userName, String userType, String userId, String orgId) {
        // 1. 验证邮箱唯一性：检查该邮箱是否已被注册
        QueryWrapper<PasswordEntity> passwordQuery = new QueryWrapper<>();
        passwordQuery.eq("email", email);
        PasswordEntity existingPassword = passwordMapper.selectOne(passwordQuery);
        if (existingPassword != null) {
            throw new BusinessException(E008);
        }

        // 2. 创建密码实体：存储加密后的密码和账户状态信息
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setEmail(email);
        passwordEntity.setPassword(passwordEncoder.encode(password));
        passwordEntity.setIsLocked(false);
        passwordEntity.setLoginStatus("ACTIVE");
        passwordEntity.setRetryCount(0);
        passwordMapper.insert(passwordEntity);

        // 3. 根据用户类型创建对应的业务用户实体
        if ("BUSINESS_USER".equals(userType)) {
            BusinessUserEntity businessUser = new BusinessUserEntity();
            businessUser.setEmail(email);
            businessUser.setName(userName);
            businessUser.setBusinessId(orgId);
            businessUser.setBusinessUserId(userId);
            businessUserMapper.insert(businessUser);
        } else if ("STAFF_USER".equals(userType)) {
            StaffUserEntity staffUser = new StaffUserEntity();
            staffUser.setEmail(email);
            staffUser.setName(userName);
            staffUser.setLocationId(orgId);
            staffUser.setStaffUserId(userId);
            staffUserMapper.insert(staffUser);
        } else {
            throw new BusinessException(E010);
        }
    }

    @Override
    public UserInfoEntity getUserByUserId(String userId) {
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfoEntity::getUserId, userId);
        UserInfoEntity userInfoEntity = vUserInfoMapper.selectOne(queryWrapper);
        if (userInfoEntity == null) {
            throw new BusinessException(E010);
        }
        return userInfoEntity;
    }

    @Override
    @Transactional
    public void updateUser(String userId, String userType, String name, String orgId) {
        if ("BUSINESS_USER".equals(userType)) {
            LambdaQueryWrapper<BusinessUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BusinessUserEntity::getBusinessUserId, userId);
            BusinessUserEntity businessUser = businessUserMapper.selectOne(queryWrapper);
            businessUser.setName(name);
            businessUser.setBusinessId(orgId);
            businessUserMapper.updateById(businessUser);
        } else if ("STAFF_USER".equals(userType)) {
            LambdaQueryWrapper<StaffUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StaffUserEntity::getStaffUserId, userId);
            StaffUserEntity staffUser = staffUserMapper.selectOne(queryWrapper);
            staffUser.setName(name);
            staffUser.setLocationId(orgId);
            staffUserMapper.updateById(staffUser);
        } else {
            throw new BusinessException(E010);
        }
    }

    @Override
    public Page<UserInfo> pageUsers(PageUserQueryRequest request) {
        // 1. 创建 MybatisPlus 分页对象
        Page<UserInfoEntity> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 2. 构建查询条件
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();

        // 用户类型过滤
        if (request.getUserType() != null && !request.getUserType().isEmpty()) {
            queryWrapper.eq(UserInfoEntity::getUserType, request.getUserType());
        }

        // 邮箱模糊查询
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            queryWrapper.like(UserInfoEntity::getEmail, request.getEmail());
        }

        // 用户名模糊查询
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            queryWrapper.like(UserInfoEntity::getUserName, request.getUserName());
        }

        // 3. 使用 MybatisPlus 分页查询
        Page<UserInfoEntity> resultPage = vUserInfoMapper.selectPage(page, queryWrapper);

        // 4. 转换为 UserInfo DTO
        Page<UserInfo> dtoPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        dtoPage.setRecords(resultPage.getRecords().stream().map(UserInfo::new).toList());

        return dtoPage;
    }

    @Override
    @Transactional
    public void deleteUser(String userId, String userType) {
        if ("BUSINESS_USER".equals(userType)) {
            LambdaQueryWrapper<BusinessUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BusinessUserEntity::getBusinessUserId, userId);
            businessUserMapper.delete(queryWrapper);
        } else if ("STAFF_USER".equals(userType)) {
            LambdaQueryWrapper<StaffUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StaffUserEntity::getStaffUserId, userId);
            staffUserMapper.delete(queryWrapper);
        } else {
            throw new BusinessException(E010);
        }
    }
}

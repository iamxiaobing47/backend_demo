package com.taco.backend_demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.dto.common.OptionItem;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.dto.user.PageUserQueryRequest;
import com.taco.backend_demo.entity.BusinessEntity;
import com.taco.backend_demo.entity.BusinessUserEntity;
import com.taco.backend_demo.entity.LocationEntity;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.StaffUserEntity;
import com.taco.backend_demo.mapper.mp.BusinessMapper;
import com.taco.backend_demo.mapper.mp.BusinessUserMapper;
import com.taco.backend_demo.mapper.mp.LocationMapper;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.StaffUserMapper;
import com.taco.backend_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E008;
import static com.taco.backend_demo.common.message.ErrorMessageCodes.E010;

/**
 * 用户服务实现类
 *
 * 【核心职责】
 * 1. 创建用户：处理用户注册，包括密码加密和用户类型分发
 * 2. 查询用户：根据用户 ID 获取用户信息
 * 3. 更新用户：修改用户基本信息
 * 4. 分页查询：支持条件筛选的用户列表查询
 * 5. 删除用户：软删除或硬删除用户记录
 *
 * 【用户类型说明】
 * - BUSINESS_USER: 业务用户，关联 business_user 表
 * - STAFF_USER: 员工用户，关联 staff_user 表
 *
 * @author taco
 */
@Service
public class UserServiceImpl extends ServiceImpl<PasswordMapper, PasswordEntity> implements UserService {

    // ==================== 依赖注入 ====================

    /**
     * 密码 Mapper：操作用户密码表（password）
     */
    @Autowired
    private PasswordMapper passwordMapper;

    /**
     * 员工用户 Mapper：操作员工用户表（staff_user）
     */
    @Autowired
    private StaffUserMapper staffUserMapper;

    /**
     * 业务用户 Mapper：操作业务用户表（business_user）
     */
    @Autowired
    private BusinessUserMapper businessUserMapper;

    /**
     * 密码编码器：负责密码加密（BCrypt）和匹配验证
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 业务 Mapper：操作用户业务表（business）
     */
    @Autowired
    private BusinessMapper businessMapper;

    /**
     * 位置 Mapper：操作用户位置表（staff_location）
     */
    @Autowired
    private LocationMapper locationMapper;

    /**
     * 【创建用户】处理用户注册
     * <p>
     * 完整流程：
     * 1. 验证邮箱唯一性：检查该邮箱是否已被注册
     * 2. 创建密码记录：存储加密后的密码和账户状态
     * 3. 根据用户类型创建对应的业务用户记录
     *
     * @param email 用户邮箱（作为唯一标识和登录账号）
     * @param password 用户密码（明文，会被加密存储）
     * @param userName 用户昵称
     * @param userType 用户类型：BUSINESS_USER 或 STAFF_USER
     * @param userId 业务用户 ID 或员工用户 ID（此处为 pk）
     * @param orgId 组织 ID：业务 ID 或位置 ID（此处为 pk）
     */
    @Override
    @Transactional
    public void createUser(String email, String password, String userName, String userType, String userId, String orgId) {
        // 【步骤 1】验证邮箱唯一性
        // 创建查询条件，检查该邮箱是否已被注册
        QueryWrapper<PasswordEntity> passwordQuery = new QueryWrapper<>();
        passwordQuery.eq("email", email);
        PasswordEntity existingPassword = passwordMapper.selectOne(passwordQuery);
        // 如果邮箱已存在，抛出异常（E008: 邮箱已注册）
        if (existingPassword != null) {
            throw new BusinessException(E008);
        }

        // 【步骤 2】创建密码实体
        // 存储加密后的密码和账户状态信息
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setEmail(email);                           // 用户邮箱
        passwordEntity.setPassword(passwordEncoder.encode(password)); // BCrypt 加密密码
        passwordEntity.setIsLocked(false);                        // 账户未锁定
        passwordEntity.setLoginStatus("ACTIVE");                  // 登录状态：活跃
        passwordEntity.setRetryCount(0);                          // 密码错误重试次数
        passwordMapper.insert(passwordEntity);

        // 【步骤 3】根据用户类型创建对应的业务用户实体
        // 不同类型的用户存储在不同的表中
        if ("BUSINESS_USER".equals(userType)) {
            // 业务用户：存储在 business_user 表
            BusinessUserEntity businessUser = new BusinessUserEntity();
            businessUser.setEmail(email);
            businessUser.setName(userName);
            businessUser.setBusinessPk(Integer.parseInt(orgId));   // 关联企业 pk
            businessUserMapper.insert(businessUser);
        } else if ("STAFF_USER".equals(userType)) {
            // 员工用户：存储在 staff_user 表
            StaffUserEntity staffUser = new StaffUserEntity();
            staffUser.setEmail(email);
            staffUser.setName(userName);
            staffUser.setLocationPk(Long.parseLong(orgId));        // 关联位置 pk
            staffUserMapper.insert(staffUser);
        } else {
            // 不支持的用户类型
            throw new BusinessException(E010);
        }
    }

    /**
     * 【查询用户】根据邮箱获取用户信息
     * <p>
     * 工作流程：
     * 1. 根据用户类型从对应表查询
     * 2. 如果未找到，抛出异常
     *
     * @param userId 用户 pk
     * @param userType 用户类型
     * @return 用户信息
     */
    @Override
    public UserInfo getUserByUserId(String userId, String userType) {
        if ("BUSINESS_USER".equals(userType)) {
            BusinessUserEntity businessUser = businessUserMapper.selectById(Long.parseLong(userId));
            if (businessUser == null) {
                throw new BusinessException(E010);
            }
            return convertToUserInfo(businessUser, userType);
        } else if ("STAFF_USER".equals(userType)) {
            StaffUserEntity staffUser = staffUserMapper.selectById(Long.parseLong(userId));
            if (staffUser == null) {
                throw new BusinessException(E010);
            }
            return convertToUserInfo(staffUser, userType);
        } else {
            throw new BusinessException(E010);
        }
    }

    /**
     * 【更新用户】修改用户基本信息
     * <p>
     * 工作流程：
     * 1. 根据用户类型确定要更新的表
     * 2. 查询用户记录
     * 3. 更新字段并保存
     *
     * @param userId 用户 pk
     * @param userType 用户类型：BUSINESS_USER 或 STAFF_USER
     * @param name 新昵称
     * @param orgId 新组织 pk
     */
    @Override
    @Transactional
    public void updateUser(String userId, String userType, String name, String orgId) {
        if ("BUSINESS_USER".equals(userType)) {
            // 业务用户更新
            BusinessUserEntity businessUser = businessUserMapper.selectById(Long.parseLong(userId));
            if (businessUser == null) {
                throw new BusinessException(E010);
            }
            businessUser.setName(name);           // 更新昵称
            businessUser.setBusinessPk(Integer.parseInt(orgId));    // 更新关联企业 pk
            businessUserMapper.updateById(businessUser);
        } else if ("STAFF_USER".equals(userType)) {
            // 员工用户更新
            StaffUserEntity staffUser = staffUserMapper.selectById(Long.parseLong(userId));
            if (staffUser == null) {
                throw new BusinessException(E010);
            }
            staffUser.setName(name);            // 更新昵称
            staffUser.setLocationPk(Long.parseLong(orgId));     // 更新关联位置 pk
            staffUserMapper.updateById(staffUser);
        } else {
            // 不支持的用户类型
            throw new BusinessException(E010);
        }
    }

    /**
     * 【分页查询】根据条件分页查询用户列表
     * <p>
     * 工作流程：
     * 1. 根据用户类型从对应表查询
     * 2. 转换为 UserInfo DTO
     * 3. 返回分页结果
     *
     * @param request 分页查询请求（包含页码、页数、筛选条件）
     * @return 分页结果（包含用户列表和总数）
     */
    @Override
    public Page<UserInfo> pageUsers(PageUserQueryRequest request) {
        if ("BUSINESS_USER".equals(request.getUserType())) {
            // 查询业务用户
            Page<BusinessUserEntity> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<BusinessUserEntity> queryWrapper = new LambdaQueryWrapper<>();

            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                queryWrapper.like(BusinessUserEntity::getEmail, request.getEmail());
            }
            if (request.getUserName() != null && !request.getUserName().isEmpty()) {
                queryWrapper.like(BusinessUserEntity::getName, request.getUserName());
            }

            Page<BusinessUserEntity> resultPage = businessUserMapper.selectPage(page, queryWrapper);
            return convertToUserInfoPage(resultPage, "BUSINESS_USER");
        } else if ("STAFF_USER".equals(request.getUserType())) {
            // 查询员工用户
            Page<StaffUserEntity> page = new Page<>(request.getPageNum(), request.getPageSize());
            LambdaQueryWrapper<StaffUserEntity> queryWrapper = new LambdaQueryWrapper<>();

            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                queryWrapper.like(StaffUserEntity::getEmail, request.getEmail());
            }
            if (request.getUserName() != null && !request.getUserName().isEmpty()) {
                queryWrapper.like(StaffUserEntity::getName, request.getUserName());
            }

            Page<StaffUserEntity> resultPage = staffUserMapper.selectPage(page, queryWrapper);
            return convertToUserInfoPage(resultPage, "STAFF_USER");
        } else {
            // 查询所有用户（合并查询）：先获取所有数据，然后在内存中分页
            LambdaQueryWrapper<BusinessUserEntity> businessQueryWrapper = new LambdaQueryWrapper<>();
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                businessQueryWrapper.like(BusinessUserEntity::getEmail, request.getEmail());
            }
            if (request.getUserName() != null && !request.getUserName().isEmpty()) {
                businessQueryWrapper.like(BusinessUserEntity::getName, request.getUserName());
            }
            List<BusinessUserEntity> allBusinessUsers = businessUserMapper.selectList(businessQueryWrapper);

            LambdaQueryWrapper<StaffUserEntity> staffQueryWrapper = new LambdaQueryWrapper<>();
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                staffQueryWrapper.like(StaffUserEntity::getEmail, request.getEmail());
            }
            if (request.getUserName() != null && !request.getUserName().isEmpty()) {
                staffQueryWrapper.like(StaffUserEntity::getName, request.getUserName());
            }
            List<StaffUserEntity> allStaffUsers = staffUserMapper.selectList(staffQueryWrapper);

            // 合并所有记录
            List<UserInfo> allRecords = new java.util.ArrayList<>();
            for (BusinessUserEntity u : allBusinessUsers) {
                allRecords.add(convertToUserInfo(u, "BUSINESS_USER"));
            }
            for (StaffUserEntity u : allStaffUsers) {
                allRecords.add(convertToUserInfo(u, "STAFF_USER"));
            }

            // 在内存中分页
            long total = allRecords.size();
            long pageNum = request.getPageNum();
            long pageSize = request.getPageSize();
            long pages = (total + pageSize - 1) / pageSize;

            List<UserInfo> pagedRecords = new java.util.ArrayList<>();
            if (pageNum <= pages && pageNum > 0) {
                int fromIndex = (int) ((pageNum - 1) * pageSize);
                int toIndex = (int) Math.min(fromIndex + pageSize, total);
                if (fromIndex < total) {
                    pagedRecords = allRecords.subList(fromIndex, toIndex);
                }
            }

            Page<UserInfo> dtoPage = new Page<>(pageNum, pageSize, total);
            dtoPage.setRecords(pagedRecords);
            return dtoPage;
        }
    }

    /**
     * 将 BusinessUserEntity 或 StaffUserEntity 的 Page 转换为 UserInfo 的 Page
     */
    private <T> Page<UserInfo> convertToUserInfoPage(Page<T> entityPage, String userType) {
        Page<UserInfo> dtoPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        dtoPage.setRecords(entityPage.getRecords().stream()
            .map(entity -> {
                if (entity instanceof BusinessUserEntity) {
                    return convertToUserInfo((BusinessUserEntity) entity, "BUSINESS_USER");
                } else {
                    return convertToUserInfo((StaffUserEntity) entity, "STAFF_USER");
                }
            })
            .toList());
        return dtoPage;
    }

    /**
     * 【删除用户】删除用户记录
     * <p>
     * 工作流程：
     * 1. 根据用户类型确定要删除的表
     * 2. 构建查询条件
     * 3. 执行删除
     *
     * @param userId 用户 pk
     * @param userType 用户类型
     */
    @Override
    @Transactional
    public void deleteUser(String userId, String userType) {
        if ("BUSINESS_USER".equals(userType)) {
            // 删除业务用户
            businessUserMapper.deleteById(Long.parseLong(userId));
        } else if ("STAFF_USER".equals(userType)) {
            // 删除员工用户
            staffUserMapper.deleteById(Long.parseLong(userId));
        } else {
            // 不支持的用户类型
            throw new BusinessException(E010);
        }
    }

    /**
     * 【获取企业列表】获取所有企业信息
     *
     * @return 企业列表
     */
    @Override
    public List<OptionItem> getBusinessList() {
        List<BusinessEntity> businesses = businessMapper.selectList(new QueryWrapper<>());
        return businesses.stream()
            .map(b -> new OptionItem(String.valueOf(b.getPk()), b.getName()))
            .collect(Collectors.toList());
    }

    /**
     * 【获取地区列表】获取所有地区信息
     *
     * @return 地区列表
     */
    @Override
    public List<OptionItem> getRegionList() {
        List<LocationEntity> locations = locationMapper.selectList(new QueryWrapper<>());
        return locations.stream()
            .map(l -> new OptionItem(String.valueOf(l.getPk()), l.getName()))
            .collect(Collectors.toList());
    }

    /**
     * 将 BusinessUserEntity 转换为 UserInfo
     */
    private UserInfo convertToUserInfo(BusinessUserEntity businessUser, String userType) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPk(businessUser.getPk());
        userInfo.setEmail(businessUser.getEmail());
        userInfo.setUserName(businessUser.getName());
        userInfo.setUserType(userType);
        userInfo.setOrgId(String.valueOf(businessUser.getBusinessPk()));
        return userInfo;
    }

    /**
     * 将 StaffUserEntity 转换为 UserInfo
     */
    private UserInfo convertToUserInfo(StaffUserEntity staffUser, String userType) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPk(staffUser.getPk());
        userInfo.setEmail(staffUser.getEmail());
        userInfo.setUserName(staffUser.getName());
        userInfo.setUserType(userType);
        userInfo.setOrgId(String.valueOf(staffUser.getLocationPk()));
        return userInfo;
    }
}

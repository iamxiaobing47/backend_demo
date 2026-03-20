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
 * 【数据库操作】
 * 继承 ServiceImpl<VUserInfoMapper, UserInfoEntity>，获得基础 CRUD 能力
 *
 * @author taco
 */
@Service
public class UserServiceImpl extends ServiceImpl<VUserInfoMapper, UserInfoEntity> implements UserService {

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
     * 用户信息视图 Mapper：查询用户综合信息（v_user_info）
     */
    @Autowired
    private VUserInfoMapper vUserInfoMapper;

    /**
     * 密码编码器：负责密码加密（BCrypt）和匹配验证
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

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
     * @param userId 业务用户 ID 或员工用户 ID
     * @param orgId 组织 ID：业务 ID 或位置 ID
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
            businessUser.setBusinessId(orgId);       // 关联企业 ID
            businessUser.setBusinessUserId(userId);  // 业务用户 ID
            businessUserMapper.insert(businessUser);
        } else if ("STAFF_USER".equals(userType)) {
            // 员工用户：存储在 staff_user 表
            StaffUserEntity staffUser = new StaffUserEntity();
            staffUser.setEmail(email);
            staffUser.setName(userName);
            staffUser.setLocationId(orgId);          // 关联位置 ID
            staffUser.setStaffUserId(userId);        // 员工用户 ID
            staffUserMapper.insert(staffUser);
        } else {
            // 不支持的用户类型
            throw new BusinessException(E010);
        }
    }

    /**
     * 【查询用户】根据用户 ID 获取用户信息
     * <p>
     * 工作流程：
     * 1. 创建查询条件
     * 2. 从用户信息视图查询
     * 3. 如果未找到，抛出异常
     *
     * @param userId 用户 ID
     * @return 用户信息实体
     */
    @Override
    public UserInfoEntity getUserByUserId(String userId) {
        // 创建 LambdaQueryWrapper，类型安全地构建查询条件
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        // 等价 SQL: WHERE user_id = ?
        queryWrapper.eq(UserInfoEntity::getUserId, userId);

        // 执行查询
        UserInfoEntity userInfoEntity = vUserInfoMapper.selectOne(queryWrapper);

        // 如果未找到，抛出异常（E010: 用户不存在）
        if (userInfoEntity == null) {
            throw new BusinessException(E010);
        }
        return userInfoEntity;
    }

    /**
     * 【更新用户】修改用户基本信息
     * <p>
     * 工作流程：
     * 1. 根据用户类型确定要更新的表
     * 2. 查询用户记录
     * 3. 更新字段并保存
     *
     * @param userId 用户 ID
     * @param userType 用户类型：BUSINESS_USER 或 STAFF_USER
     * @param name 新昵称
     * @param orgId 新组织 ID
     */
    @Override
    @Transactional
    public void updateUser(String userId, String userType, String name, String orgId) {
        if ("BUSINESS_USER".equals(userType)) {
            // 业务用户更新
            LambdaQueryWrapper<BusinessUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BusinessUserEntity::getBusinessUserId, userId);
            BusinessUserEntity businessUser = businessUserMapper.selectOne(queryWrapper);
            businessUser.setName(name);           // 更新昵称
            businessUser.setBusinessId(orgId);    // 更新关联企业 ID
            businessUserMapper.updateById(businessUser);
        } else if ("STAFF_USER".equals(userType)) {
            // 员工用户更新
            LambdaQueryWrapper<StaffUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StaffUserEntity::getStaffUserId, userId);
            StaffUserEntity staffUser = staffUserMapper.selectOne(queryWrapper);
            staffUser.setName(name);            // 更新昵称
            staffUser.setLocationId(orgId);     // 更新关联位置 ID
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
     * 1. 创建 MybatisPlus 分页对象
     * 2. 构建动态查询条件（支持多条件组合）
     * 3. 执行分页查询
     * 4. 转换为 DTO 返回
     *
     * @param request 分页查询请求（包含页码、页数、筛选条件）
     * @return 分页结果（包含用户列表和总数）
     */
    @Override
    public Page<UserInfo> pageUsers(PageUserQueryRequest request) {
        // 【步骤 1】创建 MybatisPlus 分页对象
        // pageNum: 当前页码（从 1 开始）
        // pageSize: 每页大小
        Page<UserInfoEntity> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 【步骤 2】构建查询条件
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();

        // 【条件 1】用户类型过滤（精确匹配）
        // 如果传入了 userType，添加 WHERE user_type = ? 条件
        if (request.getUserType() != null && !request.getUserType().isEmpty()) {
            queryWrapper.eq(UserInfoEntity::getUserType, request.getUserType());
        }

        // 【条件 2】邮箱模糊查询
        // 如果传入了 email，添加 WHERE email LIKE '%?%' 条件
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            queryWrapper.like(UserInfoEntity::getEmail, request.getEmail());
        }

        // 【条件 3】用户名模糊查询
        // 如果传入了 userName，添加 WHERE user_name LIKE '%?%' 条件
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            queryWrapper.like(UserInfoEntity::getUserName, request.getUserName());
        }

        // 【步骤 3】使用 MybatisPlus 分页查询
        // 自动执行：SELECT * FROM v_user_info WHERE ... LIMIT ?, ?
        // 同时自动查询总数：SELECT COUNT(*) FROM v_user_info WHERE ...
        Page<UserInfoEntity> resultPage = vUserInfoMapper.selectPage(page, queryWrapper);

        // 【步骤 4】转换为 UserInfo DTO
        // 将 Entity 转换为 DTO，便于前后端数据隔离
        Page<UserInfo> dtoPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        // 使用 Stream API 批量转换记录
        dtoPage.setRecords(resultPage.getRecords().stream().map(UserInfo::new).toList());

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
     * @param userId 用户 ID
     * @param userType 用户类型：BUSINESS_USER 或 STAFF_USER
     */
    @Override
    @Transactional
    public void deleteUser(String userId, String userType) {
        if ("BUSINESS_USER".equals(userType)) {
            // 删除业务用户
            LambdaQueryWrapper<BusinessUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BusinessUserEntity::getBusinessUserId, userId);
            businessUserMapper.delete(queryWrapper);
        } else if ("STAFF_USER".equals(userType)) {
            // 删除员工用户
            LambdaQueryWrapper<StaffUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StaffUserEntity::getStaffUserId, userId);
            staffUserMapper.delete(queryWrapper);
        } else {
            // 不支持的用户类型
            throw new BusinessException(E010);
        }
    }
}

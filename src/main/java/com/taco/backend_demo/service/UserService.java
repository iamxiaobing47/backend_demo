package com.taco.backend_demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.dto.user.PageUserQueryRequest;
import com.taco.backend_demo.entity.UserInfoEntity;

/**
 * 用户服务接口
 */
public interface UserService extends IService<UserInfoEntity> {

    /**
     * 创建用户
     * @param email 用户邮箱
     * @param password 用户密码
     * @param userName 用户名
     * @param userType 用户类型
     * @param userId 用户 ID
     * @param orgId 组织 ID
     */
    void createUser(String email, String password, String userName, String userType, String userId, String orgId);

    /**
     * 根据用户 ID 获取用户信息
     * @param userId 用户 ID
     * @return 用户信息实体
     */
    UserInfoEntity getUserByUserId(String userId);

    /**
     * 更新用户信息
     * @param userId 用户 ID
     * @param userType 用户类型
     * @param name 用户名
     * @param orgId 组织 ID
     */
    void updateUser(String userId, String userType, String name, String orgId);

    /**
     * 分页查询用户列表
     * @param request 分页查询请求
     * @return 分页结果
     */
    IPage<UserInfo> pageUsers(PageUserQueryRequest request);

    /**
     * 删除用户
     * @param userId 用户 ID
     * @param userType 用户类型
     */
    void deleteUser(String userId, String userType);
}

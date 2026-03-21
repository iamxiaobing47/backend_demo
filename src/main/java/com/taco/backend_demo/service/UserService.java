package com.taco.backend_demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taco.backend_demo.dto.common.OptionItem;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.dto.user.PageUserQueryRequest;
import com.taco.backend_demo.entity.PasswordEntity;

/**
 * 用户服务接口
 */
public interface UserService extends IService<PasswordEntity> {

    /**
     * 创建用户
     * @param email 用户邮箱
     * @param password 用户密码
     * @param userName 用户名
     * @param userType 用户类型
     * @param userId 用户 ID（可选，为空时自动生成）
     * @param orgId 组织 ID
     */
    void createUser(String email, String password, String userName, String userType, String userId, String orgId);

    /**
     * 根据用户 ID 获取用户信息
     * @param userId 用户 pk
     * @param userType 用户类型
     * @return 用户信息
     */
    UserInfo getUserByUserId(String userId, String userType);

    /**
     * 更新用户信息
     * @param userId 用户 pk
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
     * @param userId 用户 pk
     * @param userType 用户类型
     */
    void deleteUser(String userId, String userType);

    /**
     * 获取企业列表
     * @return 企业列表
     */
    java.util.List<OptionItem> getBusinessList();

    /**
     * 获取地区列表
     * @return 地区列表
     */
    java.util.List<OptionItem> getRegionList();
}

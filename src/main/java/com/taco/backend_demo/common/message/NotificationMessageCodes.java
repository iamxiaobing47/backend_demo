package com.taco.backend_demo.common.message;

/**
 * 通知消息码常量类（N 前缀）
 * - 成功消息
 * - 认证相关通知
 */
public final class NotificationMessageCodes {

    // 防止实例化
    private NotificationMessageCodes() {}

    // ==================== 成功操作 ====================
    public static final String N001 = "N001"; // 创建成功
    public static final String N002 = "N002"; // 更新成功
    public static final String N003 = "N003"; // 删除成功

    // ==================== 认证相关 ====================
    public static final String N021 = "N021"; // 登出成功
}

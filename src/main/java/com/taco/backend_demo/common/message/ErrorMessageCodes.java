package com.taco.backend_demo.common.message;

/**
 * 错误消息码常量类：定义系统中所有错误消息的唯一标识码（E 前缀）
 * 消息码分类管理：按功能模块和错误类型分组，便于维护和查找
 * 国际化支持：消息码对应 messages.properties 中的键值，支持多语言处理
 */
public final class ErrorMessageCodes {

    // 私有构造函数：防止外部实例化此类
    private ErrorMessageCodes() {}

    // ==================== 认证与授权错误 (E001-E009) ====================
    public static final String E001 = "E001"; // 用户名或密码错误
    public static final String E002 = "E002"; // 用户账户已被锁定
    public static final String E003 = "E003"; // 认证失败
    public static final String E004 = "E004"; // 密码验证失败
    public static final String E006 = "E006"; // 刷新令牌无效
    public static final String E007 = "E007"; // 未认证，请先登录
    public static final String E008 = "E008"; // 未授权，请联系管理员

    // ==================== 参数验证错误 (E010-E019) ====================
    public static final String E010 = "E010"; // 参数错误
    public static final String E014 = "E014"; // 字段{0}不能为空
    public static final String E015 = "E015"; // 字段{0}格式错误
    public static final String E016 = "E016"; // 字段{0}长度必须在{1}到{2}之间

    // ==================== 系统级错误 (E060-E999) ====================
    public static final String E999 = "E999"; // 系统异常，请联系管理员
}

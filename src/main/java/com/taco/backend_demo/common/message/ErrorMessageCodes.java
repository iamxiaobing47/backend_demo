package com.taco.backend_demo.common.message;

/**
 * 1. 错误消息码常量类：定义系统中所有错误消息的唯一标识码（E前缀）
 * 2. 消息码分类管理：按功能模块和错误类型进行分组，便于维护和查找
 * 3. 国际化支持：消息码对应messages.properties中的键值，支持多语言处理
 */
public final class ErrorMessageCodes {
    
    // 1. 私有构造函数：防止外部实例化此类
    private ErrorMessageCodes() {}
    
    // ==================== 1. 认证与授权错误 (E001-E009) ====================
    public static final String E001 = "E001"; // 用户名或密码错误
    public static final String E002 = "E002"; // 用户账户已被锁定
    public static final String E003 = "E003"; // 认证失败
    public static final String E004 = "E004"; // 密码验证失败
    public static final String E005 = "E005"; // 密码已过期
    public static final String E006 = "E006"; // 刷新令牌无效
    public static final String E007 = "E007"; // 未认证，请先登录
    public static final String E008 = "E008"; // 未授权，请联系管理员
    
    // ==================== 2. 参数验证错误 (E010-E019) ====================
    public static final String E010 = "E010"; // 参数错误
    public static final String E011 = "E011"; // 必填字段缺失
    public static final String E012 = "E012"; // 格式错误
    public static final String E013 = "E013"; // 数据重复
    public static final String E014 = "E014"; // 字段{0}不能为空
    public static final String E015 = "E015"; // 字段{0}格式错误
    public static final String E016 = "E016"; // 字段{0}长度必须在{1}到{2}之间
    public static final String E017 = "E017"; // 字段{0}必须大于{1}
    public static final String E018 = "E018"; // 字段{0}必须小于{1}
    
    // ==================== 3. 资源操作错误 (E020-E029) ====================
    public static final String E020 = "E020"; // 资源未找到
    public static final String E021 = "E021"; // {0}未找到
    public static final String E022 = "E022"; // {0}已存在
    public static final String E023 = "E023"; // {0}与{1}冲突
    
    // ==================== 4. 业务逻辑错误 (E030-E039) ====================
    public static final String E030 = "E030"; // 权限不足
    public static final String E031 = "E031"; // 用户{0}权限不足
    public static final String E032 = "E032"; // 用户{0}会话已过期
    public static final String E033 = "E033"; // 会话已过期
    public static final String E034 = "E034"; // 禁止访问
    public static final String E035 = "E035"; // 请求错误
    
    // ==================== 5. 操作执行错误 (E040-E049) ====================
    public static final String E040 = "E040"; // 创建失败
    public static final String E041 = "E041"; // 更新失败  
    public static final String E042 = "E042"; // 删除失败
    public static final String E043 = "E043"; // {0}创建失败
    public static final String E044 = "E044"; // {0}更新失败
    public static final String E045 = "E045"; // {0}删除失败
    
    // ==================== 6. 数据处理错误 (E050-E059) ====================
    public static final String E050 = "E050"; // 数据处理失败
    public static final String E051 = "E051"; // 数据冲突
    public static final String E052 = "E052"; // 处理超时
    
    // ==================== 7. 系统级错误 (E060-E999) ====================
    public static final String E060 = "E060"; // 系统维护中
    public static final String E061 = "E061"; // 服务不可用
    public static final String E062 = "E062"; // 网络错误
    public static final String E999 = "E999"; // 系统异常，请联系管理员
}

package com.taco.backend_demo.common.message;

public class Messages {
    // 基础状态消息
    public static final String MSG_0001 = "成功";
    public static final String MSG_0002 = "通知";
    public static final String MSG_0003 = "错误";
    public static final String MSG_0004 = "资源不存在";
    public static final String MSG_0005 = "未授权";
    public static final String MSG_0006 = "错误请求";

    // 验证相关消息
    public static final String MSG_0007 = "参数错误";
    public static final String MSG_0008 = "必填字段缺失";
    public static final String MSG_0009 = "格式错误";
    public static final String MSG_0010 = "数据重复";
    // 带参数的验证消息
    public static final String MSG_0026 = "字段 {0} 不能为空";
    public static final String MSG_0027 = "字段 {0} 格式错误";
    public static final String MSG_0028 = "字段 {0} 长度必须在 {1} 到 {2} 之间";
    public static final String MSG_0029 = "字段 {0} 必须大于 {1}";
    public static final String MSG_0030 = "字段 {0} 必须小于 {1}";

    // 权限相关消息
    public static final String MSG_0011 = "权限不足";
    public static final String MSG_0012 = "禁止访问";
    public static final String MSG_0013 = "会话过期";
    // 带参数的权限消息
    public static final String MSG_0031 = "用户 {0} 权限不足";
    public static final String MSG_0032 = "用户 {0} 会话已过期";

    // 操作相关消息
    public static final String MSG_0014 = "创建成功";
    public static final String MSG_0015 = "更新成功";
    public static final String MSG_0016 = "删除成功";
    public static final String MSG_0017 = "创建失败";
    public static final String MSG_0018 = "更新失败";
    public static final String MSG_0019 = "删除失败";
    // 带参数的操作消息
    public static final String MSG_0033 = "{0} 创建成功";
    public static final String MSG_0034 = "{0} 更新成功";
    public static final String MSG_0035 = "{0} 删除成功";
    public static final String MSG_0036 = "{0} 创建失败";
    public static final String MSG_0037 = "{0} 更新失败";
    public static final String MSG_0038 = "{0} 删除失败";

    // 数据处理相关消息
    public static final String MSG_0020 = "数据处理失败";
    public static final String MSG_0021 = "数据冲突";
    public static final String MSG_0022 = "处理超时";
    // 带参数的数据消息
    public static final String MSG_0039 = "{0} 不存在";
    public static final String MSG_0040 = "{0} 已存在";
    public static final String MSG_0041 = "{0} 与 {1} 冲突";

    // 系统相关消息
    public static final String MSG_0023 = "系统维护中";
    public static final String MSG_0024 = "服务不可用";
    public static final String MSG_0025 = "网络异常";
}
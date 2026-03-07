package com.taco.backend_demo.common.message;

public class Messages {
    // 基础状态消息
    public static final String CODE_001 = "MSG_001";
    public static final String MSG_001 = "成功";

    public static final String CODE_002 = "MSG_002";
    public static final String MSG_002 = "通知";

    public static final String CODE_003 = "MSG_003";
    public static final String MSG_003 = "错误";

    public static final String CODE_004 = "MSG_004";
    public static final String MSG_004 = "资源不存在";

    public static final String CODE_005 = "MSG_005";
    public static final String MSG_005 = "未授权";

    public static final String CODE_006 = "MSG_006";
    public static final String MSG_006 = "错误请求";

    // 验证相关消息
    public static final String CODE_007 = "MSG_007";
    public static final String MSG_007 = "参数错误";

    public static final String CODE_008 = "MSG_008";
    public static final String MSG_008 = "必填字段缺失";

    public static final String CODE_009 = "MSG_009";
    public static final String MSG_009 = "格式错误";

    public static final String CODE_010 = "MSG_010";
    public static final String MSG_010 = "数据重复";

    // 带参数的验证消息
    public static final String CODE_026 = "MSG_026";
    public static final String MSG_026 = "字段 {0} 不能为空";

    public static final String CODE_027 = "MSG_027";
    public static final String MSG_027 = "字段 {0} 格式错误";

    public static final String CODE_028 = "MSG_028";
    public static final String MSG_028 = "字段 {0} 长度必须在 {1} 到 {2} 之间";

    public static final String CODE_029 = "MSG_029";
    public static final String MSG_029 = "字段 {0} 必须大于 {1}";

    public static final String CODE_030 = "MSG_030";
    public static final String MSG_030 = "字段 {0} 必须小于 {1}";

    // 权限相关消息
    public static final String CODE_011 = "MSG_011";
    public static final String MSG_011 = "权限不足";

    public static final String CODE_012 = "MSG_012";
    public static final String MSG_012 = "禁止访问";

    public static final String CODE_013 = "MSG_013";
    public static final String MSG_013 = "会话过期";

    // 带参数的权限消息
    public static final String CODE_031 = "MSG_031";
    public static final String MSG_031 = "用户 {0} 权限不足";

    public static final String CODE_032 = "MSG_032";
    public static final String MSG_032 = "用户 {0} 会话已过期";

    // 操作相关消息
    public static final String CODE_014 = "MSG_014";
    public static final String MSG_014 = "创建成功";

    public static final String CODE_015 = "MSG_015";
    public static final String MSG_015 = "更新成功";

    public static final String CODE_016 = "MSG_016";
    public static final String MSG_016 = "删除成功";

    public static final String CODE_017 = "MSG_017";
    public static final String MSG_017 = "创建失败";

    public static final String CODE_018 = "MSG_018";
    public static final String MSG_018 = "更新失败";

    public static final String CODE_019 = "MSG_019";
    public static final String MSG_019 = "删除失败";

    // 数据处理相关消息
    public static final String CODE_020 = "MSG_020";
    public static final String MSG_020 = "数据处理失败";

    public static final String CODE_021 = "MSG_021";
    public static final String MSG_021 = "数据冲突";

    public static final String CODE_022 = "MSG_022";
    public static final String MSG_022 = "处理超时";

    // 系统相关消息
    public static final String CODE_023 = "MSG_023";
    public static final String MSG_023 = "系统维护中";

    public static final String CODE_024 = "MSG_024";
    public static final String MSG_024 = "服务不可用";

    public static final String CODE_025 = "MSG_025";
    public static final String MSG_025 = "网络异常";

    // 带参数的操作消息
    public static final String CODE_033 = "MSG_033";
    public static final String MSG_033 = "{0} 创建成功";

    public static final String CODE_034 = "MSG_034";
    public static final String MSG_034 = "{0} 更新成功";

    public static final String CODE_035 = "MSG_035";
    public static final String MSG_035 = "{0} 删除成功";

    public static final String CODE_036 = "MSG_036";
    public static final String MSG_036 = "{0} 创建失败";

    public static final String CODE_037 = "MSG_037";
    public static final String MSG_037 = "{0} 更新失败";

    public static final String CODE_038 = "MSG_038";
    public static final String MSG_038 = "{0} 删除失败";

    // 带参数的数据消息
    public static final String CODE_039 = "MSG_039";
    public static final String MSG_039 = "{0} 不存在";

    public static final String CODE_040 = "MSG_040";
    public static final String MSG_040 = "{0} 已存在";

    public static final String CODE_041 = "MSG_041";
    public static final String MSG_041 = "{0} 与 {1} 冲突";

    // HTTP
    public static final String CODE_401 = "MSG_401";
    public static final String MSG_401 = "未认证，请先登录";

    public static final String CODE_403 = "MSG_403";
    public static final String MSG_403 = "未授权，请联系管理员";

    public static final String CODE_999 = "MSG_999";
    public static final String MSG_999 = "系统异常，请联系管理员";

    // 认证相关消息
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String LOGIN_FAIL = "LOGIN_FAIL";
    public static final String REGISTER_SUCCESS = "REGISTER_SUCCESS";
    public static final String REGISTER_FAIL = "REGISTER_FAIL";
    public static final String LOGOUT_SUCCESS = "LOGOUT_SUCCESS";
    public static final String GET_USER_SUCCESS = "GET_USER_SUCCESS";

}

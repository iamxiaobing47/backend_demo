package com.taco.backend_demo.common.message;

import java.text.MessageFormat;

/**
 * 消息处理工具类
 */
public class MessageUtils {
    /**
     * 格式化消息，替换占位符
     * @param message 消息模板，包含 {0}, {1} 等占位符
     * @param args 替换参数
     * @return 格式化后的消息
     */
    public static String format(String message, Object... args) {
        return MessageFormat.format(message, args);
    }
}
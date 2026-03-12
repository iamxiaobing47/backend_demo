package com.taco.backend_demo.common.message;

/**
 * 1. 消息码工具类：提供消息码的验证、分类和类型检查功能
 * 2. 格式验证：验证消息码是否符合[E|W|N|M]XXX的标准格式
 * 3. 类型识别：支持错误、警告、通知、消息四种消息类型的识别
 */
public final class MessageCodeUtils {
    
    // 1. 私有构造函数：防止外部实例化此类
    private MessageCodeUtils() {}
    
    /**
     * 1. 验证消息码格式：检查消息码是否符合标准格式[E|W|N|M][3位数字]
     * @param code 待验证的消息码
     * @return true表示格式有效，false表示格式无效
     */
    public static boolean isValidMessageCode(String code) {
        if (code == null || code.length() != 4) {
            return false;
        }
        
        char category = code.charAt(0);
        String numberPart = code.substring(1);
        
        // 1. 验证消息码类别字符
        if (category != 'E' && category != 'W' && category != 'N' && category != 'M') {
            return false;
        }
        
        // 2. 验证数字部分是否为3位数字
        try {
            int number = Integer.parseInt(numberPart);
            return number >= 0 && number <= 999;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 2. 获取消息码类别：返回消息码的类别字符
     * @param code 消息码
     * @return 类别字符（'E'=错误, 'W'=警告, 'N'=通知, 'M'=消息）
     * @throws IllegalArgumentException 当消息码格式无效时抛出异常
     */
    public static char getCategory(String code) {
        if (!isValidMessageCode(code)) {
            throw new IllegalArgumentException("Invalid message code: " + code);
        }
        return code.charAt(0);
    }
    
    /**
     * 3. 检查是否为错误码：判断消息码是否属于错误类型
     * @param code 消息码
     * @return true表示是错误码（以'E'开头），false表示不是
     */
    public static boolean isError(String code) {
        return code != null && code.startsWith("E");
    }
    
    /**
     * 4. 检查是否为警告码：判断消息码是否属于警告类型
     * @param code 消息码
     * @return true表示是警告码（以'W'开头），false表示不是
     */
    public static boolean isWarning(String code) {
        return code != null && code.startsWith("W");
    }
    
    /**
     * 5. 检查是否为通知码：判断消息码是否属于通知类型
     * @param code 消息码
     * @return true表示是通知码（以'N'开头），false表示不是
     */
    public static boolean isNotification(String code) {
        return code != null && code.startsWith("N");
    }
    
    /**
     * 6. 检查是否为普通消息码：判断消息码是否属于普通消息类型
     * @param code 消息码
     * @return true表示是消息码（以'M'开头），false表示不是
     */
    public static boolean isMessage(String code) {
        return code != null && code.startsWith("M");
    }
}

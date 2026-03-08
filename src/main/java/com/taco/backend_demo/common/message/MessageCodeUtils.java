package com.taco.backend_demo.common.message;

/**
 * Utility class for working with message codes.
 * Provides helper methods to validate and categorize message codes.
 */
public final class MessageCodeUtils {
    
    // Prevent instantiation
    private MessageCodeUtils() {}
    
    /**
     * Validates if a message code follows the expected format: [E|W|N|M][3-digit number]
     * @param code the message code to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidMessageCode(String code) {
        if (code == null || code.length() != 4) {
            return false;
        }
        
        char category = code.charAt(0);
        String numberPart = code.substring(1);
        
        // Check if category is valid
        if (category != 'E' && category != 'W' && category != 'N' && category != 'M') {
            return false;
        }
        
        // Check if number part is numeric and 3 digits
        try {
            int number = Integer.parseInt(numberPart);
            return number >= 0 && number <= 999;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Gets the category of a message code.
     * @param code the message code
     * @return the category character ('E', 'W', 'N', or 'M')
     * @throws IllegalArgumentException if the code is invalid
     */
    public static char getCategory(String code) {
        if (!isValidMessageCode(code)) {
            throw new IllegalArgumentException("Invalid message code: " + code);
        }
        return code.charAt(0);
    }
    
    /**
     * Checks if a message code represents an error.
     * @param code the message code
     * @return true if it's an error code (starts with 'E')
     */
    public static boolean isError(String code) {
        return code != null && code.startsWith("E");
    }
    
    /**
     * Checks if a message code represents a warning.
     * @param code the message code
     * @return true if it's a warning code (starts with 'W')
     */
    public static boolean isWarning(String code) {
        return code != null && code.startsWith("W");
    }
    
    /**
     * Checks if a message code represents a notification.
     * @param code the message code
     * @return true if it's a notification code (starts with 'N')
     */
    public static boolean isNotification(String code) {
        return code != null && code.startsWith("N");
    }
    
    /**
     * Checks if a message code represents a general message.
     * @param code the message code
     * @return true if it's a message code (starts with 'M')
     */
    public static boolean isMessage(String code) {
        return code != null && code.startsWith("M");
    }
}
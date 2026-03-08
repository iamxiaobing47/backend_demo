package com.taco.backend_demo.common.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Message text mappings for all message codes.
 */
public final class MessageTexts {
    
    // Prevent instantiation
    private MessageTexts() {}
    
    private static final Map<String, String> MESSAGE_TEXTS;
    
    static {
        Map<String, String> messages = new HashMap<>();
        
        // ==================== Authentication & Authorization ====================
        messages.put("E001", "Username or password incorrect");
        messages.put("E002", "User account locked");
        messages.put("E003", "Authentication failed");
        messages.put("E004", "Password verification failed");
        messages.put("E005", "Password expired");
        messages.put("E006", "Invalid refresh token");
        messages.put("E007", "Not authenticated, please login first");
        messages.put("E008", "Not authorized, please contact administrator");
        
        // ==================== Validation Errors ====================
        messages.put("E010", "Parameter error");
        messages.put("E011", "Required field missing");
        messages.put("E012", "Format error");
        messages.put("E013", "Data duplicate");
        messages.put("E014", "Field {0} cannot be empty");
        messages.put("E015", "Field {0} format error");
        messages.put("E016", "Field {0} length must be between {1} and {2}");
        messages.put("E017", "Field {0} must be greater than {1}");
        messages.put("E018", "Field {0} must be less than {1}");
        
        // ==================== Resource Errors ====================
        messages.put("E020", "Resource not found");
        messages.put("E021", "{0} not found");
        messages.put("E022", "{0} already exists");
        messages.put("E023", "{0} conflicts with {1}");
        
        // ==================== Business Logic Errors ====================
        messages.put("E030", "Permission denied");
        messages.put("E031", "User {0} permission denied");
        messages.put("E032", "User {0} session expired");
        messages.put("E033", "Session expired");
        messages.put("E034", "Forbidden access");
        messages.put("E035", "Bad request");
        
        // ==================== Operation Errors ====================
        messages.put("E040", "Create failed");
        messages.put("E041", "Update failed");
        messages.put("E042", "Delete failed");
        messages.put("E043", "{0} create failed");
        messages.put("E044", "{0} update failed");
        messages.put("E045", "{0} delete failed");
        
        // ==================== Data Processing Errors ====================
        messages.put("E050", "Data processing failed");
        messages.put("E051", "Data conflict");
        messages.put("E052", "Processing timeout");
        
        // ==================== System Errors ====================
        messages.put("E060", "System maintenance");
        messages.put("E061", "Service unavailable");
        messages.put("E062", "Network error");
        messages.put("E999", "System exception, please contact administrator");
        
        MESSAGE_TEXTS = Collections.unmodifiableMap(messages);
    }
    
    /**
     * Gets the message text for a given message code.
     * @param code the message code
     * @return the message text, or null if not found
     */
    public static String getMessageText(String code) {
        return MESSAGE_TEXTS.get(code);
    }
    
    /**
     * Gets the message text for a given message code, with a default fallback.
     * @param code the message code
     * @param defaultText the default text to return if code is not found
     * @return the message text, or the default text if not found
     */
    public static String getMessageText(String code, String defaultText) {
        String message = MESSAGE_TEXTS.get(code);
        return message != null ? message : defaultText;
    }
}
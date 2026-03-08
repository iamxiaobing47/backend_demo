package com.taco.backend_demo.common.message;

/**
 * Message code constants organized by category:
 * - E: Error (system errors, validation failures, authentication issues)
 * - W: Warning (potential issues, data conflicts, retry scenarios)  
 * - N: Notification (success messages, informational updates)
 * - M: Message (general messages that don't fit other categories)
 * 
 * Format: [Category][3-digit number] (e.g., E001, W001, N001, M001)
 */
public final class MessageCodes {
    
    // Prevent instantiation
    private MessageCodes() {}
}
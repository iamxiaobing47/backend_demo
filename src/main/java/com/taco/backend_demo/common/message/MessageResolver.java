package com.taco.backend_demo.common.message;

import java.text.MessageFormat;

/**
 * Message resolver for getting formatted message text from message codes.
 */
public final class MessageResolver {
    
    // Prevent instantiation
    private MessageResolver() {}
    
    /**
     * Resolves a message code to its formatted message text.
     * @param messageCode the message code
     * @param args the arguments to format into the message
     * @return the formatted message text, or the message code if not found
     */
    public static String resolveMessage(String messageCode, Object... args) {
        String messageText = MessageTexts.getMessageText(messageCode);
        if (messageText == null) {
            return messageCode; // Return code as fallback
        }
        
        if (args.length == 0) {
            return messageText;
        }
        
        // Format the message with arguments
        try {
            return MessageFormat.format(messageText, args);
        } catch (Exception e) {
            // If formatting fails, return the raw message text
            return messageText;
        }
    }
    
    /**
     * Resolves a message code to its message text without formatting.
     * @param messageCode the message code
     * @return the message text, or the message code if not found
     */
    public static String resolveMessage(String messageCode) {
        String messageText = MessageTexts.getMessageText(messageCode);
        return messageText != null ? messageText : messageCode;
    }
}
package com.taco.backend_demo.common.message;

/**
 * General message codes (M prefix)
 * - Messages that don't fit into other categories
 * - Neutral informational messages
 * - Custom business messages
 */
public final class MessageMessageCodes {
    
    // Prevent instantiation
    private MessageMessageCodes() {}
    
    // ==================== General Information ====================
    public static final String M001 = "M001"; // Information
    public static final String M002 = "M002"; // Please note
    public static final String M003 = "M003"; // Additional information
    
    // ==================== User Interface Messages ====================
    public static final String M010 = "M010"; // Form submitted successfully
    public static final String M011 = "M011"; // Changes saved
    public static final String M012 = "M012"; // Please review your input
    
    // ==================== System Messages ====================
    public static final String M020 = "M020"; // System is processing your request
    public static final String M021 = "M021"; // Background task initiated
    public static final String M022 = "M022"; // Cache refreshed
    
    // ==================== Custom Business Messages ====================
    public static final String M030 = "M030"; // Welcome message
    public static final String M031 = "M031"; // Terms and conditions updated
    public static final String M032 = "M032"; // New features available
}
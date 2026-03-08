package com.taco.backend_demo.common.message;

/**
 * Warning message codes (W prefix)
 * - Potential issues that don't block operation
 * - Data conflicts that can be resolved
 * - Retry scenarios
 * - Performance or resource warnings
 */
public final class WarningMessageCodes {
    
    // Prevent instantiation
    private WarningMessageCodes() {}
    
    // ==================== Data Warnings ====================
    public static final String W001 = "W001"; // Data conflict detected
    public static final String W002 = "W002"; // {0} conflicts with existing data
    public static final String W003 = "W003"; // Duplicate entry detected, using existing
    
    // ==================== Validation Warnings ====================
    public static final String W010 = "W010"; // Field {0} value may cause performance issues
    public static final String W011 = "W011"; // Large data size detected, may impact performance
    public static final String W012 = "W012"; // Deprecated field usage detected
    
    // ==================== Security Warnings ====================
    public static final String W020 = "W020"; // Multiple failed login attempts detected
    public static final String W021 = "W021"; // Account will be locked after {0} more attempts
    public static final String W022 = "W022"; // Password expiration approaching
    
    // ==================== System Warnings ====================
    public static final String W030 = "W030"; // Low disk space
    public static final String W031 = "W031"; // High memory usage
    public static final String W032 = "W032"; // Database connection pool nearly full
    public static final String W033 = "W033"; // API rate limit approaching
    
    // ==================== Business Logic Warnings ====================
    public static final String W040 = "W040"; // Operation completed with partial success
    public static final String W041 = "W041"; // Some records were skipped due to validation
    public static final String W042 = "W042"; // Default values applied for missing fields
}
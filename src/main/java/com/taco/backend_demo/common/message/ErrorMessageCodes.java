package com.taco.backend_demo.common.message;

/**
 * Error message codes (E prefix)
 * - System errors
 * - Validation failures  
 * - Authentication/Authorization issues
 * - Business logic errors
 */
public final class ErrorMessageCodes {
    
    // Prevent instantiation
    private ErrorMessageCodes() {}
    
    // ==================== Authentication & Authorization ====================
    public static final String E001 = "E001"; // Username or password incorrect
    public static final String E002 = "E002"; // User account locked
    public static final String E003 = "E003"; // Authentication failed
    public static final String E004 = "E004"; // Password verification failed
    public static final String E005 = "E005"; // Password expired
    public static final String E006 = "E006"; // Invalid refresh token
    public static final String E007 = "E007"; // Not authenticated, please login first
    public static final String E008 = "E008"; // Not authorized, please contact administrator
    
    // ==================== Validation Errors ====================
    public static final String E010 = "E010"; // Parameter error
    public static final String E011 = "E011"; // Required field missing
    public static final String E012 = "E012"; // Format error
    public static final String E013 = "E013"; // Data duplicate
    public static final String E014 = "E014"; // Field {0} cannot be empty
    public static final String E015 = "E015"; // Field {0} format error
    public static final String E016 = "E016"; // Field {0} length must be between {1} and {2}
    public static final String E017 = "E017"; // Field {0} must be greater than {1}
    public static final String E018 = "E018"; // Field {0} must be less than {1}
    
    // ==================== Resource Errors ====================
    public static final String E020 = "E020"; // Resource not found
    public static final String E021 = "E021"; // {0} not found
    public static final String E022 = "E022"; // {0} already exists
    public static final String E023 = "E023"; // {0} conflicts with {1}
    
    // ==================== Business Logic Errors ====================
    public static final String E030 = "E030"; // Permission denied
    public static final String E031 = "E031"; // User {0} permission denied
    public static final String E032 = "E032"; // User {0} session expired
    public static final String E033 = "E033"; // Session expired
    public static final String E034 = "E034"; // Forbidden access
    public static final String E035 = "E035"; // Bad request
    
    // ==================== Operation Errors ====================
    public static final String E040 = "E040"; // Create failed
    public static final String E041 = "E041"; // Update failed  
    public static final String E042 = "E042"; // Delete failed
    public static final String E043 = "E043"; // {0} create failed
    public static final String E044 = "E044"; // {0} update failed
    public static final String E045 = "E045"; // {0} delete failed
    
    // ==================== Data Processing Errors ====================
    public static final String E050 = "E050"; // Data processing failed
    public static final String E051 = "E051"; // Data conflict
    public static final String E052 = "E052"; // Processing timeout
    
    // ==================== System Errors ====================
    public static final String E060 = "E060"; // System maintenance
    public static final String E061 = "E061"; // Service unavailable
    public static final String E062 = "E062"; // Network error
    public static final String E999 = "E999"; // System exception, please contact administrator
}
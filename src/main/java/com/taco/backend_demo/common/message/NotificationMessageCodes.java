package com.taco.backend_demo.common.message;

/**
 * Notification message codes (N prefix)
 * - Success messages
 * - Informational updates
 * - Confirmation messages
 * - Status updates
 */
public final class NotificationMessageCodes {
    
    // Prevent instantiation
    private NotificationMessageCodes() {}
    
    // ==================== Success Messages ====================
    public static final String N001 = "N001"; // Success
    public static final String N002 = "N002"; // Operation completed successfully
    public static final String N003 = "N003"; // Data saved successfully
    
    // ==================== Operation Success ====================
    public static final String N010 = "N010"; // Create successful
    public static final String N011 = "N011"; // Update successful
    public static final String N012 = "N012"; // Delete successful
    public static final String N013 = "N013"; // {0} created successfully
    public static final String N014 = "N014"; // {0} updated successfully
    public static final String N015 = "N015"; // {0} deleted successfully
    
    // ==================== Authentication Success ====================
    public static final String N020 = "N020"; // Login successful
    public static final String N021 = "N021"; // Logout successful
    public static final String N022 = "N022"; // Registration successful
    public static final String N023 = "N023"; // Token refreshed successfully
    public static final String N024 = "N024"; // Test user created successfully
    public static final String N025 = "N025"; // User information retrieved successfully
    
    // ==================== System Notifications ====================
    public static final String N030 = "N030"; // System is operational
    public static final String N031 = "N031"; // Maintenance completed
    public static final String N032 = "N032"; // Configuration updated successfully
    
    // ==================== Business Logic Notifications ====================
    public static final String N040 = "N040"; // Process initiated
    public static final String N041 = "N041"; // Process completed
    public static final String N042 = "N042"; // Batch operation completed successfully
    public static final String N043 = "N043"; // All records processed successfully
}
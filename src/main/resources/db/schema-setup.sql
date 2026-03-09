-- Database schema setup for dynamic menu management system

-- Create navigations table if it doesn't exist
CREATE TABLE IF NOT EXISTS navigations (
    pk BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    icon VARCHAR(100),
    sort_order INT DEFAULT 0,
    parent_id BIGINT,
    enabled BOOLEAN DEFAULT TRUE,
    user_type VARCHAR(50), -- 'employee', 'business_owner', or NULL for all
    business_owner_restrictions VARCHAR(255), -- Comma-separated business owner IDs, NULL means no restriction
    location_restrictions VARCHAR(255), -- Comma-separated location IDs, NULL means no restriction
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create business_users table if it doesn't exist
CREATE TABLE IF NOT EXISTS business_users (
    pk BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_user_id BIGINT UNIQUE,
    business_id BIGINT,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    position VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create staff_users table if it doesn't exist
CREATE TABLE IF NOT EXISTS staff_users (
    pk BIGINT PRIMARY KEY AUTO_INCREMENT,
    staff_user_id BIGINT UNIQUE,
    location_id BIGINT,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    position VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create passwords table if it doesn't exist
CREATE TABLE IF NOT EXISTS passwords (
    pk BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    is_locked BOOLEAN DEFAULT FALSE,
    login_status VARCHAR(50) DEFAULT 'ACTIVE',
    retry_count INT DEFAULT 0,
    locked_at TIMESTAMP NULL,
    last_login_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create tokens table if it doesn't exist
CREATE TABLE IF NOT EXISTS tokens (
    pk BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(500) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_navigations_enabled_parent ON navigations(enabled, parent_id);
CREATE INDEX IF NOT EXISTS idx_navigations_user_type ON navigations(user_type);
CREATE INDEX IF NOT EXISTS idx_business_users_email ON business_users(email);
CREATE INDEX IF NOT EXISTS idx_staff_users_email ON staff_users(email);
CREATE INDEX IF NOT EXISTS idx_passwords_email ON passwords(email);
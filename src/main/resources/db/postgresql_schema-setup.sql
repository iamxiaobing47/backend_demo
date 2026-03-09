-- PostgreSQL schema setup for dynamic menu management system

-- Create navigations table if it doesn't exist
CREATE TABLE IF NOT EXISTS navigations (
    pk BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    icon VARCHAR(100),
    sort_order INTEGER DEFAULT 0,
    parent_id BIGINT,
    enabled BOOLEAN DEFAULT TRUE,
    user_type VARCHAR(50), -- 'employee', 'business_owner', or NULL for all
    business_owner_restrictions VARCHAR(255), -- Comma-separated business owner IDs, NULL means no restriction
    location_restrictions VARCHAR(255), -- Comma-separated location IDs, NULL means no restriction
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create business_users table if it doesn't exist
CREATE TABLE IF NOT EXISTS business_users (
    pk BIGSERIAL PRIMARY KEY,
    business_user_id BIGINT UNIQUE,
    business_id BIGINT,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    position VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create staff_users table if it doesn't exist
CREATE TABLE IF NOT EXISTS staff_users (
    pk BIGSERIAL PRIMARY KEY,
    staff_user_id BIGINT UNIQUE,
    location_id BIGINT,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    position VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create passwords table if it doesn't exist
CREATE TABLE IF NOT EXISTS passwords (
    pk BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    is_locked BOOLEAN DEFAULT FALSE,
    login_status VARCHAR(50) DEFAULT 'ACTIVE',
    retry_count INTEGER DEFAULT 0,
    locked_at TIMESTAMP WITH TIME ZONE NULL,
    last_login_at TIMESTAMP WITH TIME ZONE NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create tokens table if it doesn't exist
CREATE TABLE IF NOT EXISTS tokens (
    pk BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(500) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_navigations_enabled_parent ON navigations(enabled, parent_id);
CREATE INDEX IF NOT EXISTS idx_navigations_user_type ON navigations(user_type);
CREATE INDEX IF NOT EXISTS idx_business_users_email ON business_users(email);
CREATE INDEX IF NOT EXISTS idx_staff_users_email ON staff_users(email);
CREATE INDEX IF NOT EXISTS idx_passwords_email ON passwords(email);

-- Update trigger for updated_at timestamp (PostgreSQL)
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for tables that need automatic updated_at updates
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'update_navigations_updated_at') THEN
        CREATE TRIGGER update_navigations_updated_at 
            BEFORE UPDATE ON navigations 
            FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'update_business_users_updated_at') THEN
        CREATE TRIGGER update_business_users_updated_at 
            BEFORE UPDATE ON business_users 
            FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'update_staff_users_updated_at') THEN
        CREATE TRIGGER update_staff_users_updated_at 
            BEFORE UPDATE ON staff_users 
            FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'update_passwords_updated_at') THEN
        CREATE TRIGGER update_passwords_updated_at 
            BEFORE UPDATE ON passwords 
            FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'update_tokens_updated_at') THEN
        CREATE TRIGGER update_tokens_updated_at 
            BEFORE UPDATE ON tokens 
            FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    END IF;
END $$;
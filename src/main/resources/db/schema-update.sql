-- Update navigations table to add new columns for business owner and location restrictions
ALTER TABLE navigations ADD COLUMN business_owner_restrictions VARCHAR(255) DEFAULT NULL COMMENT 'Comma-separated business owner IDs, NULL means no restriction';
ALTER TABLE navigations ADD COLUMN location_restrictions VARCHAR(255) DEFAULT NULL COMMENT 'Comma-separated location IDs, NULL means no restriction';

-- Update existing navigation records to set appropriate restrictions based on the demo requirements

-- 事业者A can see "项目列表"
UPDATE navigations SET business_owner_restrictions = 'A', location_restrictions = NULL WHERE name = '项目列表';

-- 事业者B can see "模板下载" 
UPDATE navigations SET business_owner_restrictions = 'B', location_restrictions = NULL WHERE name = '模板下载';

-- 据点Y employees can see "项目列表"
UPDATE navigations SET location_restrictions = 'Y' WHERE name = '项目列表' AND location_restrictions IS NULL;

-- 据点X should have no restrictions (NULL for both) to see all menus
-- This applies to all menus that don't have specific restrictions

-- Create sample users for demo purposes
-- Business owners
INSERT INTO passwords (pk, email, password_hash, is_locked, login_status, retry_count, created_at, updated_at) VALUES
(101, 'owner_a@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, NOW(), NOW()),
(102, 'owner_b@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, NOW(), NOW());

INSERT INTO business_users (pk, business_user_id, business_id, email, name, position, created_at, updated_at) VALUES
(201, 1, 1, 'owner_a@example.com', 'Business Owner A', 'Owner', NOW(), NOW()),
(202, 2, 2, 'owner_b@example.com', 'Business Owner B', 'Owner', NOW(), NOW());

-- Staff users
INSERT INTO passwords (pk, email, password_hash, is_locked, login_status, retry_count, created_at, updated_at) VALUES
(103, 'staff_x@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, NOW(), NOW()),
(104, 'staff_y@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, NOW(), NOW());

INSERT INTO staff_users (pk, staff_user_id, location_id, email, name, position, created_at, updated_at) VALUES
(301, 1, 1, 'staff_x@example.com', 'Staff X', 'Manager', NOW(), NOW()),  -- 据点X
(302, 2, 2, 'staff_y@example.com', 'Staff Y', 'Employee', NOW(), NOW()); -- 据点Y
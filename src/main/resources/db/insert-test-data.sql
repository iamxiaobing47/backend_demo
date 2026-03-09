-- Insert test data for dynamic menu management system

-- Clear existing data (optional)
DELETE FROM navigations WHERE pk >= 1;
DELETE FROM business_users WHERE pk >= 1;
DELETE FROM staff_users WHERE pk >= 1;
DELETE FROM passwords WHERE pk >= 100;

-- Insert navigation/menu data based on the requirements
INSERT INTO navigations (pk, name, path, icon, sort_order, parent_id, enabled, user_type, business_owner_restrictions, location_restrictions, created_at, updated_at) VALUES
(1, '首页', '/home', 'mdi-home', 1, NULL, 1, NULL, NULL, NULL, NOW(), NOW()),
(2, '项目列表', '/projectlist', 'mdi-folder-multiple', 2, NULL, 1, NULL, 'A', 'Y', NOW(), NOW()), -- Available for 事业者A and 据点Y
(3, '文件模板下载', '/template', 'mdi-download', 3, NULL, 1, NULL, 'B', NULL, NOW(), NOW()), -- Available for 事业者B
(4, '文件上传', '/upload', 'mdi-upload', 4, NULL, 1, NULL, NULL, NULL, NOW(), NOW()), -- Available for 据点X (all menus)
(5, '处理结果', '/result', 'mdi-file-document', 5, NULL, 1, NULL, NULL, NULL, NOW(), NOW()); -- Available for 据点X (all menus)

-- Update the project list to be available to location X as well (since 据点X gets all menus)
INSERT INTO navigations (pk, name, path, icon, sort_order, parent_id, enabled, user_type, business_owner_restrictions, location_restrictions, created_at, updated_at) 
VALUES (6, '项目列表', '/projectlist', 'mdi-folder-multiple', 2, NULL, 1, NULL, NULL, 'X', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
name = VALUES(name),
path = VALUES(path),
icon = VALUES(icon),
sort_order = VALUES(sort_order),
parent_id = VALUES(parent_id),
enabled = VALUES(enabled),
user_type = VALUES(user_type),
business_owner_restrictions = VALUES(business_owner_restrictions),
location_restrictions = VALUES(location_restrictions),
updated_at = VALUES(updated_at);

-- Insert sample business owners (事业者)
INSERT INTO passwords (pk, email, password_hash, is_locked, login_status, retry_count, created_at, updated_at) VALUES
(101, 'owner_a@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, NOW(), NOW()),
(102, 'owner_b@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, NOW(), NOW());

INSERT INTO business_users (pk, business_user_id, business_id, email, name, position, created_at, updated_at) VALUES
(201, 1, 1, 'owner_a@example.com', 'Business Owner A', 'Owner', NOW(), NOW()),  -- 事业者A
(202, 2, 2, 'owner_b@example.com', 'Business Owner B', 'Owner', NOW(), NOW()); -- 事业者B

-- Insert sample staff users (职员)
INSERT INTO passwords (pk, email, password_hash, is_locked, login_status, retry_count, created_at, updated_at) VALUES
(103, 'staff_x@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, NOW(), NOW()),
(104, 'staff_y@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, NOW(), NOW());

INSERT INTO staff_users (pk, staff_user_id, location_id, email, name, position, created_at, updated_at) VALUES
(301, 1, 1, 'staff_x@example.com', 'Staff X', 'Manager', NOW(), NOW()),  -- 据点X (gets all menus)
(302, 2, 2, 'staff_y@example.com', 'Staff Y', 'Employee', NOW(), NOW()); -- 据点Y (limited menus)

-- Create a test user for general access
INSERT INTO passwords (pk, email, password_hash, is_locked, login_status, retry_count, created_at, updated_at) VALUES
(105, 'test@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, NOW(), NOW());

-- Test credentials summary:
-- Business Owner A (事业者A):
--   Email: owner_a@example.com
--   Password: (hashed) - can access "项目列表"
--
-- Business Owner B (事业者B):
--   Email: owner_b@example.com  
--   Password: (hashed) - can access "文件模板下载"
--
-- Staff at Location X (据点X):
--   Email: staff_x@example.com
--   Password: (hashed) - can access ALL menus
--
-- Staff at Location Y (据点Y):
--   Email: staff_y@example.com
--   Password: (hashed) - can access "项目列表" only
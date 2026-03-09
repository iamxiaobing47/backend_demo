-- PostgreSQL test data insertion for dynamic menu management system

-- Clear existing data (optional)
TRUNCATE TABLE navigations RESTART IDENTITY CASCADE;
TRUNCATE TABLE business_users RESTART IDENTITY CASCADE;
TRUNCATE TABLE staff_users RESTART IDENTITY CASCADE;
TRUNCATE TABLE passwords RESTART IDENTITY CASCADE;

-- Insert navigation/menu data based on the requirements
INSERT INTO navigations (name, path, icon, sort_order, parent_id, enabled, user_type, business_owner_restrictions, location_restrictions, created_at, updated_at) VALUES
('首页', '/home', 'mdi-home', 1, NULL, TRUE, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('项目列表', '/projectlist', 'mdi-folder-multiple', 2, NULL, TRUE, NULL, '1', '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Available for 事业者A (ID=1) and 据点Y (ID=2)
('文件模板下载', '/template', 'mdi-download', 3, NULL, TRUE, NULL, '2', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Available for 事业者B (ID=2)
('文件上传', '/upload', 'mdi-upload', 4, NULL, TRUE, NULL, NULL, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Available for 据点X (ID=1, all menus)
('处理结果', '/result', 'mdi-file-document', 5, NULL, TRUE, NULL, NULL, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- Available for 据点X (ID=1, all menus)

-- Insert sample business owners (事业者)
INSERT INTO passwords (email, password_hash, is_locked, login_status, retry_count, created_at, updated_at) VALUES
('owner_a@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('owner_b@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO business_users (business_user_id, business_id, email, name, position, created_at, updated_at) VALUES
(1, 1, 'owner_a@example.com', 'Business Owner A', 'Owner', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 事业者A
(2, 2, 'owner_b@example.com', 'Business Owner B', 'Owner', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- 事业者B

-- Insert sample staff users (职员)
INSERT INTO passwords (email, password_hash, is_locked, login_status, retry_count, created_at, updated_at) VALUES
('staff_x@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('staff_y@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO staff_users (staff_user_id, location_id, email, name, position, created_at, updated_at) VALUES
(1, 1, 'staff_x@example.com', 'Staff X', 'Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 据点X (gets all menus as location_id=1)
(2, 2, 'staff_y@example.com', 'Staff Y', 'Employee', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- 据点Y (limited menus as location_id=2)

-- Create a test user for general access
INSERT INTO passwords (email, password_hash, is_locked, login_status, retry_count, created_at, updated_at) VALUES
('test@example.com', '$2a$10$DOWyfy6G4GiB0.xz5J0RbOcZ.yQxPwZqJZQdFfQo5lHhM6kKp3Y8.', FALSE, 'ACTIVE', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Test credentials summary:
-- Business Owner A (事业者A):
--   Email: owner_a@example.com
--   Password: password123 - can access "项目列表"
--
-- Business Owner B (事业者B):
--   Email: owner_b@example.com  
--   Password: password123 - can access "文件模板下载"
--
-- Staff at Location X (据点X):
--   Email: staff_x@example.com
--   Password: password123 - can access ALL menus
--
-- Staff at Location Y (据点Y):
--   Email: staff_y@example.com
--   Password: password123 - can access "项目列表" only
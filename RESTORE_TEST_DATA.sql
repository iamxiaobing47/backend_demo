-- RESTORE TEST DATA - PostgreSQL version
-- Use the create-test-user API endpoint to create users with proper password encryption
-- Then run this SQL to set up the business/staff associations

-- First, truncate only the association tables (keep passwords intact)
TRUNCATE TABLE business_users RESTART IDENTITY CASCADE;
TRUNCATE TABLE staff_users RESTART IDENTITY CASCADE;
TRUNCATE TABLE navigations RESTART IDENTITY CASCADE;

-- Insert navigation/menu data based on the requirements
INSERT INTO navigations (name, path, icon, sort_order, parent_id, enabled, user_type, business_owner_restrictions, location_restrictions, created_at, updated_at) VALUES
('首页', '/home', 'mdi-home', 1, NULL, TRUE, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('项目列表', '/projectlist', 'mdi-folder-multiple', 2, NULL, TRUE, NULL, '1', '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Available for 事业者A (ID=1) and 据点Y (ID=2)
('文件模板下载', '/template', 'mdi-download', 3, NULL, TRUE, NULL, '2', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Available for 事业者B (ID=2)
('文件上传', '/upload', 'mdi-upload', 4, NULL, TRUE, NULL, NULL, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Available for 据点X (ID=1, all menus)
('处理结果', '/result', 'mdi-file-document', 5, NULL, TRUE, NULL, NULL, '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- Available for 据点X (ID=1, all menus)

-- Create business users (事业者) - You need to create these users via the API first
INSERT INTO business_users (business_user_id, business_id, email, name, position, created_at, updated_at) VALUES
(1, 1, 'owner_a@example.com', 'Business Owner A', 'Owner', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 事业者A
(2, 2, 'owner_b@example.com', 'Business Owner B', 'Owner', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- 事业者B

-- Create staff users (职员) - You need to create these users via the API first
INSERT INTO staff_users (staff_user_id, location_id, email, name, position, created_at, updated_at) VALUES
(1, 1, 'staff_x@example.com', 'Staff X', 'Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 据点X (gets all menus as location_id=1)
(2, 2, 'staff_y@example.com', 'Staff Y', 'Employee', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- 据点Y (limited menus as location_id=2)

-- Test credentials:
-- Business Owner A: owner_a@example.com / password123
-- Business Owner B: owner_b@example.com / password123  
-- Staff at Location X: staff_x@example.com / password123
-- Staff at Location Y: staff_y@example.com / password123
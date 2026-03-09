-- 导航菜单表
CREATE TABLE navigations (
    pk BIGSERIAL PRIMARY KEY,
    navigation_id BIGINT UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL COMMENT '导航名称',
    path VARCHAR(200) NOT NULL COMMENT '路由路径',
    icon VARCHAR(50) COMMENT '图标',
    sort_order INTEGER DEFAULT 0 COMMENT '排序序号',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID，0表示顶级菜单',
    enabled BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    user_type VARCHAR(20) COMMENT '用户类型限制(business_user, staff_user)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_navigations_parent_id ON navigations(parent_id);
CREATE INDEX idx_navigations_enabled ON navigations(enabled);
CREATE INDEX idx_navigations_sort_order ON navigations(sort_order);
CREATE INDEX idx_navigations_user_type ON navigations(user_type);

-- 为事业者用户插入示例导航数据
INSERT INTO navigations (navigation_id, name, path, icon, sort_order, parent_id, enabled, user_type) VALUES
(1, '首页', '/', 'mdi-home', 1, 0, true, 'business_user'),
(2, '我的事业', '/business', 'mdi-briefcase', 2, 0, true, 'business_user'),
(3, '事业详情', '/business/detail', 'mdi-information', 1, 2, true, 'business_user'),
(4, '事业报告', '/business/reports', 'mdi-chart-bar', 2, 2, true, 'business_user'),
(5, '用户管理', '/users', 'mdi-account', 3, 0, true, 'business_user'),
(6, '事业者用户', '/users/business-users', 'mdi-account-tie', 1, 5, true, 'business_user'),
(7, '职员用户', '/users/staff-users', 'mdi-account-multiple', 2, 5, true, 'business_user'),
(8, '设置', '/settings', 'mdi-cog', 4, 0, true, 'business_user'),
(9, '个人资料', '/settings/profile', 1, 8, true, 'business_user');

-- 为职员用户插入示例导航数据
INSERT INTO navigations (navigation_id, name, path, icon, sort_order, parent_id, enabled, user_type) VALUES
(10, '首页', '/', 'mdi-home', 1, 0, true, 'staff_user'),
(11, '据点管理', '/locations', 'mdi-map-marker', 2, 0, true, 'staff_user'),
(12, '据点列表', '/locations/list', 'mdi-view-list', 1, 11, true, 'staff_user'),
(13, '工作报表', '/reports', 'mdi-clipboard-text', 3, 0, true, 'staff_user'),
(14, '日常报告', '/reports/daily', 'mdi-file-document', 1, 13, true, 'staff_user'),
(15, '周报', '/reports/weekly', 'mdi-calendar-week', 2, 13, true, 'staff_user'),
(16, '用户管理', '/users', 'mdi-account', 4, 0, true, 'staff_user'),
(17, '职员用户', '/users/staff', 'mdi-account-circle', 1, 16, true, 'staff_user'),
(18, '设置', '/settings', 'mdi-cog', 5, 0, true, 'staff_user');
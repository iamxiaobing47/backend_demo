# 动态菜单管理系统测试指南

## 测试前准备

### 1. 启动后端服务

```bash
cd ../backend_demo
./gradlew bootRun
```

### 2. 启动前端服务

```bash
cd ../frontend_demo
npm run dev
```

### 3. 数据库初始化

确保以下SQL脚本已在数据库中执行：

#### Schema Setup (执行一次)

```sql
-- 位于: src/main/resources/db/schema-setup.sql
```

#### Test Data Insertion (执行一次)

```sql
-- 位于: src/main/resources/db/insert-test-data.sql
```

## 测试账号信息

| 用户类型  | 邮箱                | 角色           | 权限说明  | 可访问菜单   | 密码        |
| --------- | ------------------- | -------------- | --------- | ------------ | ----------- |
| 事业者A   | owner_a@example.com | business_owner | 事业者A   | 项目列表     | password123 |
| 事业者B   | owner_b@example.com | business_owner | 事业者B   | 文件模板下载 | password123 |
| 据点X职员 | staff_x@example.com | employee       | 据点X员工 | 所有菜单     | password123 |
| 据点Y职员 | staff_y@example.com | employee       | 据点Y员工 | 项目列表     | password123 |

## 功能测试步骤

### 1. 事业者A登录测试

**步骤：**

1. 访问前端页面 (通常为 http://localhost:5173)
2. 点击"登录"
3. 输入邮箱: `owner_a@example.com`
4. 输入密码: `password123`
5. 登录后观察左侧菜单

**预期结果：**

- 菜单中只显示 "首页" 和 "项目列表"

### 2. 事业者B登录测试

**步骤：**

1. 访问前端页面
2. 输入邮箱: `owner_b@example.com`
3. 输入密码: `password123`
4. 登录后观察左侧菜单

**预期结果：**

- 菜单中只显示 "首页" 和 "文件模板下载"

### 3. 据点X职员登录测试

**步骤：**

1. 访问前端页面
2. 输入邮箱: `staff_x@example.com`
3. 输入密码: `password123`
4. 登录后观察左侧菜单

**预期结果：**

- 菜单中显示 "首页", "项目列表", "文件模板下载", "文件上传", "处理结果" (所有菜单)

### 4. 据点Y职员登录测试

**步骤：**

1. 访问前端页面
2. 输入邮箱: `staff_y@example.com`
3. 输入密码: `password123`
4. 登录后观察左侧菜单

**预期结果：**

- 菜单中只显示 "首页" 和 "项目列表"

## 后端API测试

### 1. 登录API测试

```bash
# 事业者A登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "owner_a@example.com", "password": "password123"}'
```

响应应包含：

- `role`: "business_owner"
- `businessOwnerId`: "1"
- `locationId`: null

### 2. 用户菜单API测试

```bash
# 登录后获取token，然后测试菜单API
curl -X GET "http://localhost:8080/api/navigations/user" \
  -H "Authorization: Bearer <your_access_token>"
```

## 验证要点

### 1. 前端验证

- [ ] 登录后菜单正确显示对应权限的选项
- [ ] 页面刷新后菜单保持不变
- [ ] 无权限的菜单不会显示
- [ ] 路由权限控制正常（无法通过URL直接访问无权限页面）

### 2. 后端验证

- [ ] 登录响应包含正确的角色和关联ID信息
- [ ] 菜单API根据用户角色返回相应菜单
- [ ] 权限验证逻辑在后端正确执行

### 3. 数据库验证

- [ ] navigations表包含business_owner_restrictions和location_restrictions字段
- [ ] business_users和staff_users表正确关联用户和组织
- [ ] 导航数据正确设置了访问限制

## 常见问题排查

### 1. 菜单未正确显示

- 检查数据库中的restriction字段是否正确设置
- 确认登录用户在business_users或staff_users表中存在

### 2. 登录失败

- 确认passwords表中有对应的用户记录
- 检查密码哈希格式是否正确

### 3. 权限控制不生效

- 确认后端NavigationService中的权限判断逻辑
- 检查数据库字段值是否正确匹配

## 额外测试场景

### 1. 角色切换测试

- 登出一个用户，登录另一个用户，确认菜单正确变化

### 2. 页面刷新测试

- 登录后刷新页面，确认菜单保持正确状态

### 3. URL直接访问测试

- 尝试直接访问无权限页面，确认被重定向或显示403错误

-- =============================================
-- 配置数据管理模块 - 表名和字段名改为英文
-- 迁移 002: 将日文罗马音表名和字段名改为英文
-- =============================================

-- =============================================
-- 1. 地域表：c_yushutsusaki_chiiki -> c_region
-- =============================================

-- 创建新的地域表
CREATE TABLE IF NOT EXISTS c_region (
    region_cd SERIAL PRIMARY KEY,              -- 地域代码 (自增主键)
    region_nm VARCHAR(100) NOT NULL,           -- 地域名
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE c_region IS '地域 (出口先地域コード)';
COMMENT ON COLUMN c_region.region_cd IS '地域コード';
COMMENT ON COLUMN c_region.region_nm IS '地域名';

-- 从旧表迁移数据到新地域表
INSERT INTO c_region (region_cd, region_nm, created_at, updated_at)
SELECT chiiki_cd, chiiki_nm, created_at, updated_at
FROM c_yushutsusaki_chiiki;

-- 更新序列
SELECT setval('c_region_region_cd_seq', (SELECT MAX(region_cd) FROM c_region));

-- =============================================
-- 2. 国家表：c_kuni -> c_country
-- =============================================

-- 创建新的国家表
CREATE TABLE IF NOT EXISTS c_country (
    country_cd SERIAL PRIMARY KEY,             -- 国代码 (自增主键)
    region_cd INT NOT NULL,                    -- 所属地域代码
    country_nm VARCHAR(100) NOT NULL,          -- 国名
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_country_region FOREIGN KEY (region_cd) REFERENCES c_region(region_cd)
);
COMMENT ON TABLE c_country IS '国 (コード)';
COMMENT ON COLUMN c_country.country_cd IS '国コード';
COMMENT ON COLUMN c_country.region_cd IS '地域コード';
COMMENT ON COLUMN c_country.country_nm IS '国名';

-- 从旧表迁移数据到新国家表
INSERT INTO c_country (country_cd, region_cd, country_nm, created_at, updated_at)
SELECT kuni_cd, chiiki_cd, kuni_nm, created_at, updated_at
FROM c_kuni;

-- 更新序列
SELECT setval('c_country_country_cd_seq', (SELECT MAX(country_cd) FROM c_country));

-- =============================================
-- 3. 品目表：c_hinmoku -> c_product
-- =============================================

-- 创建新的品目表
CREATE TABLE IF NOT EXISTS c_product (
    product_cd SERIAL PRIMARY KEY,             -- 品目代码 (自增主键)
    product_nm VARCHAR(100) NOT NULL,          -- 品目名
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE c_product IS '品目 (コード)';
COMMENT ON COLUMN c_product.product_cd IS '品目コード';
COMMENT ON COLUMN c_product.product_nm IS '品目名';

-- 从旧表迁移数据到新品目表（删除 hinmoku_en 字段）
INSERT INTO c_product (product_cd, product_nm, created_at, updated_at)
SELECT hinmoku_cd, hinmoku_nm, created_at, updated_at
FROM c_hinmoku;

-- 更新序列
SELECT setval('c_product_product_cd_seq', (SELECT MAX(product_cd) FROM c_product));

-- =============================================
-- 4. 申请书模板表：p_ikkatsu_youshiki -> p_application_template
-- =============================================

-- 创建新的申请书模板表（关联地域、国家、产品）
CREATE TABLE IF NOT EXISTS p_application_template (
    template_id SERIAL PRIMARY KEY,            -- 模板 ID(自增主键)
    region_cd INT NOT NULL,                    -- 地域代码
    country_cd INT NOT NULL,                   -- 国代码
    product_cd INT NOT NULL,                   -- 品目代码
    template_nm VARCHAR(100) NOT NULL,         -- 模板名称
    file_path VARCHAR(200) NOT NULL,           -- 文件路径
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_template_region FOREIGN KEY (region_cd) REFERENCES c_region(region_cd),
    CONSTRAINT fk_template_country FOREIGN KEY (country_cd) REFERENCES c_country(country_cd),
    CONSTRAINT fk_template_product FOREIGN KEY (product_cd) REFERENCES c_product(product_cd)
);
COMMENT ON TABLE p_application_template IS '申請書テンプレート定義';
COMMENT ON COLUMN p_application_template.template_id IS 'テンプレート ID';
COMMENT ON COLUMN p_application_template.region_cd IS '地域コード';
COMMENT ON COLUMN p_application_template.country_cd IS '国コード';
COMMENT ON COLUMN p_application_template.product_cd IS '品目コード';
COMMENT ON COLUMN p_application_template.template_nm IS 'テンプレート名';
COMMENT ON COLUMN p_application_template.file_path IS 'ファイルパス';

-- 从旧表迁移数据到新模板表（添加 region_cd 关联）
INSERT INTO p_application_template (template_id, region_cd, country_cd, product_cd, template_nm, file_path, created_at, updated_at)
SELECT
    y.youshiki_id,
    k.chiiki_cd,  -- 从国家表关联获取地域代码
    y.kuni_cd,
    y.hinmoku_cd,
    y.youshiki_nm,
    y.file_path,
    y.created_at,
    y.updated_at
FROM p_ikkatsu_youshiki y
INNER JOIN c_kuni k ON y.kuni_cd = k.kuni_cd;

-- 更新序列
SELECT setval('p_application_template_template_id_seq', (SELECT MAX(template_id) FROM p_application_template));

-- =============================================
-- 5. 创建索引
-- =============================================

CREATE INDEX IF NOT EXISTS idx_country_region ON c_country(region_cd);
CREATE INDEX IF NOT EXISTS idx_template_region ON p_application_template(region_cd);
CREATE INDEX IF NOT EXISTS idx_template_country ON p_application_template(country_cd);
CREATE INDEX IF NOT EXISTS idx_template_product ON p_application_template(product_cd);

-- =============================================
-- 6. 删除旧表（可选，确认数据迁移成功后执行）
-- =============================================

-- DROP TABLE IF EXISTS p_ikkatsu_youshiki;
-- DROP TABLE IF EXISTS c_hinmoku;
-- DROP TABLE IF EXISTS c_kuni;
-- DROP TABLE IF EXISTS c_yushutsusaki_chiiki;

-- =============================================
-- 7. 修改 navigation 表 - 删除 english_name 字段
-- =============================================

-- 删除 english_name 字段
ALTER TABLE navigation DROP COLUMN IF EXISTS english_name;

-- 将 chinese_name 改名为 name
ALTER TABLE navigation RENAME COLUMN chinese_name TO name;

COMMENT ON COLUMN navigation.name IS '导航菜单名称';

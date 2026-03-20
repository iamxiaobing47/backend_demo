-- =============================================
-- 配置数据管理模块 - 数据库表结构
-- 新人学习用简化版
-- =============================================

-- 1. 地域表
CREATE TABLE IF NOT EXISTS c_yushutsusaki_chiiki (
    chiiki_cd SERIAL PRIMARY KEY,              -- 地域代码 (自增主键)
    chiiki_nm VARCHAR(100) NOT NULL,           -- 地域名
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP   -- 更新时间
);
COMMENT ON TABLE c_yushutsusaki_chiiki IS '輸出先地域 (コード)';
COMMENT ON COLUMN c_yushutsusaki_chiiki.chiiki_cd IS '地域コード';
COMMENT ON COLUMN c_yushutsusaki_chiiki.chiiki_nm IS '地域名';

-- 2. 国家表
CREATE TABLE IF NOT EXISTS c_kuni (
    kuni_cd SERIAL PRIMARY KEY,                -- 国代码 (自增主键)
    chiiki_cd INT NOT NULL,                    -- 所属地域代码
    kuni_nm VARCHAR(50) NOT NULL,              -- 国名
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_kuni_chiiki FOREIGN KEY (chiiki_cd) REFERENCES c_yushutsusaki_chiiki(chiiki_cd)
);
COMMENT ON TABLE c_kuni IS '国 (コード)';
COMMENT ON COLUMN c_kuni.kuni_cd IS '国コード';
COMMENT ON COLUMN c_kuni.chiiki_cd IS '輸出先地域コード';
COMMENT ON COLUMN c_kuni.kuni_nm IS '国名';

-- 3. 品目表
CREATE TABLE IF NOT EXISTS c_hinmoku (
    hinmoku_cd SERIAL PRIMARY KEY,             -- 品目代码 (自增主键)
    hinmoku_nm VARCHAR(100) NOT NULL,          -- 品目名
    hinmoku_en VARCHAR(100),                   -- 品目英文名
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE c_hinmoku IS '品目 (コード)';
COMMENT ON COLUMN c_hinmoku.hinmoku_cd IS '品目コード';
COMMENT ON COLUMN c_hinmoku.hinmoku_nm IS '品目名';
COMMENT ON COLUMN c_hinmoku.hinmoku_en IS '品目英語名';

-- 4. 申请书模板表
CREATE TABLE IF NOT EXISTS p_ikkatsu_youshiki (
    youshiki_id SERIAL PRIMARY KEY,            -- 样式 ID(自增主键)
    kuni_cd INT NOT NULL,                      -- 国代码
    hinmoku_cd INT NOT NULL,                   -- 品目代码
    youshiki_nm VARCHAR(100) NOT NULL,         -- 样式名称
    file_path VARCHAR(200) NOT NULL,           -- 文件路径
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_youshiki_kuni FOREIGN KEY (kuni_cd) REFERENCES c_kuni(kuni_cd),
    CONSTRAINT fk_youshiki_hinmoku FOREIGN KEY (hinmoku_cd) REFERENCES c_hinmoku(hinmoku_cd)
);
COMMENT ON TABLE p_ikkatsu_youshiki IS '一括様式定義';
COMMENT ON COLUMN p_ikkatsu_youshiki.youshiki_id IS '様式 ID';
COMMENT ON COLUMN p_ikkatsu_youshiki.kuni_cd IS '国コード';
COMMENT ON COLUMN p_ikkatsu_youshiki.hinmoku_cd IS '品目コード';
COMMENT ON COLUMN p_ikkatsu_youshiki.youshiki_nm IS '様式表示名称';
COMMENT ON COLUMN p_ikkatsu_youshiki.file_path IS '様式ファイルパス';

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_kuni_chiiki ON c_kuni(chiiki_cd);
CREATE INDEX IF NOT EXISTS idx_youshiki_kuni ON p_ikkatsu_youshiki(kuni_cd);
CREATE INDEX IF NOT EXISTS idx_youshiki_hinmoku ON p_ikkatsu_youshiki(hinmoku_cd);

-- 插入示例数据
INSERT INTO c_yushutsusaki_chiiki (chiiki_nm) VALUES
    ('アジア'),
    ('ヨーロッパ'),
    ('北米'),
    ('南米'),
    ('アフリカ'),
    ('オセアニア');

INSERT INTO c_kuni (chiiki_cd, kuni_nm) VALUES
    (1, '日本'),
    (1, '中国'),
    (1, '韓国'),
    (2, 'イギリス'),
    (2, 'フランス'),
    (2, 'ドイツ'),
    (3, 'アメリカ'),
    (3, 'カナダ'),
    (4, 'ブラジル'),
    (5, '南アフリカ'),
    (6, 'オーストラリア');

INSERT INTO c_hinmoku (hinmoku_nm, hinmoku_en) VALUES
    ('農産物', 'Agricultural Products'),
    ('水産物', 'Marine Products'),
    ('畜産物', 'Livestock Products'),
    ('加工食品', 'Processed Foods');

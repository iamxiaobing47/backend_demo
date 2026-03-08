# 事业者用户表

```sql
-- public.business_users definition

-- Drop table

-- DROP TABLE public.business_users;

CREATE TABLE public.business_users (
	pk serial4 NOT NULL, -- 主键
	business_user_id serial4 NOT NULL, -- 事业者用户编号(业务主键)
	business_id int4 NOT NULL, -- 关联事业者编号(业务主键)
	email varchar(255) NOT NULL, -- 关联邮箱
	"name" varchar(100) NOT NULL, -- 用户姓名
	"position" varchar(100) NULL, -- 职位
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 创建时间
	updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 更新时间
	CONSTRAINT business_users_business_user_id_key UNIQUE (business_user_id),
	CONSTRAINT business_users_pkey PRIMARY KEY (pk)
);
CREATE INDEX idx_business_users_business_id ON public.business_users USING btree (business_id);
CREATE INDEX idx_business_users_email ON public.business_users USING btree (email);
COMMENT ON TABLE public.business_users IS '事业者用户表';

-- Column comments

COMMENT ON COLUMN public.business_users.pk IS '主键';
COMMENT ON COLUMN public.business_users.business_user_id IS '事业者用户编号(业务主键)';
COMMENT ON COLUMN public.business_users.business_id IS '关联事业者编号(业务主键)';
COMMENT ON COLUMN public.business_users.email IS '关联邮箱';
COMMENT ON COLUMN public.business_users."name" IS '用户姓名';
COMMENT ON COLUMN public.business_users."position" IS '职位';
COMMENT ON COLUMN public.business_users.created_at IS '创建时间';
COMMENT ON COLUMN public.business_users.updated_at IS '更新时间';
```
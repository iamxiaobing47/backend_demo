# staff_users

```sql
-- public.staff_users definition

-- Drop table

-- DROP TABLE public.staff_users;

CREATE TABLE public.staff_users (
                                    pk serial4 NOT NULL, -- 主键
                                    staff_user_id serial4 NOT NULL, -- 职员用户编号(业务主键)
                                    location_id int4 NOT NULL, -- 关联据点编号(业务主键)
                                    email varchar(255) NOT NULL, -- 关联邮箱
                                    "name" varchar(100) NOT NULL, -- 职员姓名
                                    "position" varchar(100) NULL, -- 职位
                                    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 创建时间
                                    updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 更新时间
                                    CONSTRAINT staff_users_pkey PRIMARY KEY (pk),
                                    CONSTRAINT staff_users_staff_user_id_key UNIQUE (staff_user_id)
);
CREATE INDEX idx_staff_users_email ON public.staff_users USING btree (email);
CREATE INDEX idx_staff_users_location_id ON public.staff_users USING btree (location_id);
COMMENT ON TABLE public.staff_users IS '职员用户表';

-- Column comments

COMMENT ON COLUMN public.staff_users.pk IS '主键';
COMMENT ON COLUMN public.staff_users.staff_user_id IS '职员用户编号(业务主键)';
COMMENT ON COLUMN public.staff_users.location_id IS '关联据点编号(业务主键)';
COMMENT ON COLUMN public.staff_users.email IS '关联邮箱';
COMMENT ON COLUMN public.staff_users."name" IS '职员姓名';
COMMENT ON COLUMN public.staff_users."position" IS '职位';
COMMENT ON COLUMN public.staff_users.created_at IS '创建时间';
COMMENT ON COLUMN public.staff_users.updated_at IS '更新时间';
```
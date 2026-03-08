# 法人表

```sql
-- public.corporations definition

-- Drop table

-- DROP TABLE public.corporations;

CREATE TABLE public.corporations (
                                     pk serial4 NOT NULL, -- 主键
                                     corporation_id serial4 NOT NULL, -- 法人编号(业务主键)
                                     "name" varchar(255) NOT NULL, -- 法人名称
                                     registration_number varchar(50) NULL, -- 法人登记号
                                     address varchar(500) NULL, -- 地址
                                     created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 创建时间
                                     updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 更新时间
                                     CONSTRAINT corporations_corporation_id_key UNIQUE (corporation_id),
                                     CONSTRAINT corporations_pkey PRIMARY KEY (pk)
);
COMMENT ON TABLE public.corporations IS '法人表';

-- Column comments

COMMENT ON COLUMN public.corporations.pk IS '主键';
COMMENT ON COLUMN public.corporations.corporation_id IS '法人编号(业务主键)';
COMMENT ON COLUMN public.corporations."name" IS '法人名称';
COMMENT ON COLUMN public.corporations.registration_number IS '法人登记号';
COMMENT ON COLUMN public.corporations.address IS '地址';
COMMENT ON COLUMN public.corporations.created_at IS '创建时间';
COMMENT ON COLUMN public.corporations.updated_at IS '更新时间';
```
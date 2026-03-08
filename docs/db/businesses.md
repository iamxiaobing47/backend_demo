# 事业者表

```sql
-- public.businesses definition

-- Drop table

-- DROP TABLE public.businesses;

CREATE TABLE public.businesses (
                                   pk serial4 NOT NULL, -- 主键
                                   business_id serial4 NOT NULL, -- 事业者编号(业务主键)
                                   corporation_id int4 NOT NULL, -- 关联法人编号(业务主键)
                                   "name" varchar(255) NOT NULL, -- 事业者名称
                                   business_number varchar(50) NULL, -- 事业者编号
                                   created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 创建时间
                                   updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 更新时间
                                   CONSTRAINT businesses_business_id_key UNIQUE (business_id),
                                   CONSTRAINT businesses_pkey PRIMARY KEY (pk)
);
CREATE INDEX idx_businesses_corporation_id ON public.businesses USING btree (corporation_id);
COMMENT ON TABLE public.businesses IS '事业者表';

-- Column comments

COMMENT ON COLUMN public.businesses.pk IS '主键';
COMMENT ON COLUMN public.businesses.business_id IS '事业者编号(业务主键)';
COMMENT ON COLUMN public.businesses.corporation_id IS '关联法人编号(业务主键)';
COMMENT ON COLUMN public.businesses."name" IS '事业者名称';
COMMENT ON COLUMN public.businesses.business_number IS '事业者编号';
COMMENT ON COLUMN public.businesses.created_at IS '创建时间';
COMMENT ON COLUMN public.businesses.updated_at IS '更新时间';
```
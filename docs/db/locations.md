# 据点表

```sql
-- public.locations definition

-- Drop table

-- DROP TABLE public.locations;

CREATE TABLE public.locations (
                                  pk serial4 NOT NULL, -- 主键
                                  location_id serial4 NOT NULL, -- 据点编号(业务主键)
                                  "name" varchar(255) NOT NULL, -- 据点名称
                                  address varchar(500) NULL, -- 地址
                                  created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 创建时间
                                  updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 更新时间
                                  CONSTRAINT locations_location_id_key UNIQUE (location_id),
                                  CONSTRAINT locations_pkey PRIMARY KEY (pk)
);
COMMENT ON TABLE public.locations IS '据点表';

-- Column comments

COMMENT ON COLUMN public.locations.pk IS '主键';
COMMENT ON COLUMN public.locations.location_id IS '据点编号(业务主键)';
COMMENT ON COLUMN public.locations."name" IS '据点名称';
COMMENT ON COLUMN public.locations.address IS '地址';
COMMENT ON COLUMN public.locations.created_at IS '创建时间';
COMMENT ON COLUMN public.locations.updated_at IS '更新时间';
```
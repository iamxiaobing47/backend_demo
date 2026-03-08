# 密码表

```sql
-- public.passwords definition

-- Drop table

-- DROP TABLE public.passwords;

CREATE TABLE public.passwords (
                                  pk serial4 NOT NULL, -- 主键
                                  email varchar(255) NOT NULL, -- 邮箱，作为登录账号
                                  password_hash varchar(255) NOT NULL, -- 加密后的密码
                                  is_locked bool DEFAULT false NULL, -- 是否被锁定
                                  login_status varchar(20) DEFAULT 'OFFLINE'::character varying NULL, -- 登录状态: OFFLINE/ONLINE
                                  retry_count int4 DEFAULT 0 NULL, -- 密码重试次数
                                  locked_at timestamp NULL, -- 锁定时间
                                  last_login_at timestamp NULL, -- 上次登录时间
                                  created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 创建时间
                                  updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 更新时间
                                  CONSTRAINT passwords_email_key UNIQUE (email),
                                  CONSTRAINT passwords_pkey PRIMARY KEY (pk)
);
CREATE INDEX idx_passwords_email ON public.passwords USING btree (email);
COMMENT ON TABLE public.passwords IS '密码表';

-- Column comments

COMMENT ON COLUMN public.passwords.pk IS '主键';
COMMENT ON COLUMN public.passwords.email IS '邮箱，作为登录账号';
COMMENT ON COLUMN public.passwords.password_hash IS '加密后的密码';
COMMENT ON COLUMN public.passwords.is_locked IS '是否被锁定';
COMMENT ON COLUMN public.passwords.login_status IS '登录状态: OFFLINE/ONLINE';
COMMENT ON COLUMN public.passwords.retry_count IS '密码重试次数';
COMMENT ON COLUMN public.passwords.locked_at IS '锁定时间';
COMMENT ON COLUMN public.passwords.last_login_at IS '上次登录时间';
COMMENT ON COLUMN public.passwords.created_at IS '创建时间';
COMMENT ON COLUMN public.passwords.updated_at IS '更新时间';
```
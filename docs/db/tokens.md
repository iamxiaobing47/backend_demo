# tokens

```sql
-- public.tokens definition

-- Drop table

-- DROP TABLE public.tokens;

CREATE TABLE public.tokens (
pk serial4 NOT NULL,
email varchar(255) NOT NULL,
refresh_token varchar(255) NOT NULL,
expires_at timestamp NOT NULL,
created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
CONSTRAINT tokens_pkey PRIMARY KEY (pk)
);
CREATE INDEX idx_tokens_email ON public.tokens USING btree (email);
CREATE INDEX idx_tokens_token ON public.tokens USING btree (refresh_token);
```
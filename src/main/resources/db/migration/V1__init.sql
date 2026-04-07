CREATE TABLE users(
id UUID primary key default gen_random_uuid(),
email VARCHAR(320) not null,
name varchar(200) not null,
provider varchar(50) not null,
provider_id varchar(255) not null,
role varchar(20) not null default 'ROLE_USER',
created_at TIMESTAMP not null DEFAULT NOW(),
updated_at TIMESTAMP not null default now(),

CONSTRAINT uq_user_email UNIQUE(email),
CONSTRAINT uq_user_provider UNIQUE(provider,provider_id),
CONSTRAINT chk_user_role CHECK (role IN ('ROLE_USER','ROLE_ADMIN'))
)
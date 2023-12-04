alter table public."user"
add column user_name VARCHAR(30) UNIQUE NOT NULL,
ADD COLUMN password VARCHAR(80)  NOT NULL;

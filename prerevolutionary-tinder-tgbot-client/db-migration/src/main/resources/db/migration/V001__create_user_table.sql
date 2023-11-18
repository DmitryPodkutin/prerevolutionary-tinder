CREATE TABLE public."user" (
    id BIGSERIAL PRIMARY KEY,
    telegram_user_id BIGINT NOT NULL,
    user_name VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(80)  NOT NULL
);
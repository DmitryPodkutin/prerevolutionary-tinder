CREATE TABLE public.profile (
    id BIGSERIAL PRIMARY KEY,
    gender VARCHAR(255),
    name VARCHAR(255),
    description_header VARCHAR(255),
    description TEXT,
    seeking VARCHAR(255),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES public."user"(id)
);
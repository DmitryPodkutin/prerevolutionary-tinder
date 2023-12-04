CREATE TABLE public."profile"  (
    id BIGSERIAL PRIMARY KEY,
    gender VARCHAR(10),
    name VARCHAR(255),
    description_header VARCHAR(255),
    description TEXT,
    looking_for VARCHAR(10),
    chat_id BIGSERIAL
);
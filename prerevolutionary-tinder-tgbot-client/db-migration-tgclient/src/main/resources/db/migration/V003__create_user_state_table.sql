create TABLE public."user_state"(
 id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    state_type VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES public."user"(id)
);
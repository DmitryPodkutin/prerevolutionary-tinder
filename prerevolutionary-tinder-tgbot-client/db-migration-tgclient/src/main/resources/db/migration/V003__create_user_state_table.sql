CREATE TABLE public."user_state" (
    user_id BIGINT,
    state_id BIGINT,
    PRIMARY KEY (user_id, state_id),
    FOREIGN KEY (user_id) REFERENCES public."user"(id),
    FOREIGN KEY (state_id) REFERENCES state(id)
);
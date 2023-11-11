CREATE TABLE user_role (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES public."user"(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);
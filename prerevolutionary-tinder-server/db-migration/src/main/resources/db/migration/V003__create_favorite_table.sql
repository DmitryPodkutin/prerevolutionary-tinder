    CREATE TABLE public.favorite (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT,
        favorite_user_id BIGINT,
        FOREIGN KEY (user_id) REFERENCES "user"(id),
        FOREIGN KEY (favorite_user_id) REFERENCES public."user"(id),
        CONSTRAINT unique_favorite UNIQUE (user_id, favorite_user_id)
    );
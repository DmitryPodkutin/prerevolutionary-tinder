CREATE TABLE public.matching_profiles_page_info (
    id bigserial NOT NULL,
    user_id int8 NOT NULL,
    current_page int4 NOT NULL,
    CONSTRAINT matching_profiles_page_info_pkey PRIMARY KEY (id),
    CONSTRAINT matching_profiles_page_info_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(id)
);
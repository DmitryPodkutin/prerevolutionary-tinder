-- Изменяем название поля
ALTER TABLE public.page_info
RENAME COLUMN current_page TO search_profile_current_page;

-- Добавляем новое поле
ALTER TABLE public.page_info
ADD COLUMN favorite_current_page int4 NOT NULL;
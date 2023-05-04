CREATE TABLE IF NOT EXISTS sites (
   id SERIAL PRIMARY KEY,
   login VARCHAR,
   password VARCHAR,
   site VARCHAR UNIQUE
);

COMMENT ON TABLE sites IS 'Сайты';
COMMENT ON COLUMN sites.id IS 'Идентификатор сайта';
COMMENT ON COLUMN sites.login IS 'Логин сайта';
COMMENT ON COLUMN sites.password IS 'Пароль сайта';
COMMENT ON COLUMN sites.site IS 'Наименование сайта';
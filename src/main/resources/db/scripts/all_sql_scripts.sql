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

----------------------------------------------------

CREATE TABLE IF NOT EXISTS urls (
   id SERIAL PRIMARY KEY,
   url VARCHAR UNIQUE,
   code VARCHAR,
   site_id BIGINT,
   total INT
);

COMMENT ON TABLE urls IS 'Ссылки';
COMMENT ON COLUMN urls.id IS 'Идентификатор ссылки';
COMMENT ON COLUMN urls.url IS 'Наименование ссылки';
COMMENT ON COLUMN urls.code IS 'Код ссылки';
COMMENT ON COLUMN urls.site_id IS 'Ссылка на сайт ссылки';
COMMENT ON COLUMN urls.total IS 'Счетчик посещений';
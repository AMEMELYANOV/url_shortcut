CREATE TABLE IF NOT EXISTS urls (
   id SERIAL PRIMARY KEY,
   url VARCHAR,
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
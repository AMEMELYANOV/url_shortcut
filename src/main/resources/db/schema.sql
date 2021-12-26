create table if not exists sites (
   id serial primary key,
   login varchar(255),
   password varchar(255),
   site varchar(255) unique
);

create table if not exists urls (
   id serial primary key,
   url varchar(255) unique,
   code varchar(255),
   site_id bigint,
   total int
);
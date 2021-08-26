drop table if exists post;
create table post (
    id      serial primary key,
    name    text,
    text    text,
    link    varchar(255) unique,
    created timestamp,
    posted  timestamp
);

create table if not exists rabbit (
    id           serial primary key not null,
    created_date timestamp
);

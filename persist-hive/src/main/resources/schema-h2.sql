drop table if exists users;

create table users (
    id int primary key,
    username varchar(64) not null
);


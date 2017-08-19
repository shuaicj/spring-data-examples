drop table if exists user;

create table user (
    id bigint primary key auto_increment,
    username varchar(32) not null unique,
    password varchar(60) not null,
    created_time timestamp not null default current_timestamp,
    updated_time timestamp not null default current_timestamp on update current_timestamp
);


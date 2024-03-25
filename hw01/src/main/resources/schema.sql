create table if not exists users (
    id bigserial,
    username varchar(50) unique,
    password varchar(100),
    firstName varchar(50),
    secondName varchar(50),
    birthDate date,
    gender varchar(10),
    interests varchar(255),
    city varchar(50),
    primary key (id)
);
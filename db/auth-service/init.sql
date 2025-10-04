create table users (
    id uuid default gen_random_uuid(),
    username varchar(20),
    password varchar(128),
    avatar varchar(20)
);
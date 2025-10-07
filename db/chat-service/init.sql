create table chats (
    chat_id uuid primary key default gen_random_uuid(),
    name varchar(25),
    avatar varchar(25)
);

create table generic_types(
    id uuid primary key,
    parent_id uuid references generic_types (id),
    name varchar(20)
);

create table ext_objects (
    ext_id uuid primary key,
    type_id uuid references generic_types(id)
);

create table users_chats (
    user_id uuid references ext_objects (ext_id),
    chat_id uuid references chats (chat_id)
);
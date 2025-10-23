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
    id uuid primary key,
    user_id uuid references ext_objects (ext_id),
    chat_id uuid references chats (chat_id)
);

create table messages (
    message_id uuid primary key default gen_random_uuid(),
    chat_id uuid not null references chats (chat_id) on delete cascade,
    sender_id uuid not null references ext_objects (ext_id),
    content text not null,
    sent_at timestamptz not null default now()
);
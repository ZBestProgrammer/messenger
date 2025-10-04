create table chats (
    id uuid default gen_random_uuid(),
    user1 uuid,
    user2 uuid
)
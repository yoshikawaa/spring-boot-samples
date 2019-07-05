create table if not exists todo (
    todo_id identity,
    todo_title varchar(30),
    finished boolean,
    created_at timestamp
);
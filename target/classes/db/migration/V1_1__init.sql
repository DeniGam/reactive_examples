create table if not exists user_account (
    id uuid primary key DEFAULT gen_random_uuid(),
    user_name varchar(256) not null unique,
    email varchar(512) not null unique ,
    first_name varchar(256),
    last_name varchar(256),
    created_when timestamp without time zone,
    updated_when timestamp with time zone
);

create table if not exists service_type(
    id uuid primary key DEFAULT gen_random_uuid(),
    name varchar(256) not null unique
);

create table if not exists user_service (
    user_id uuid not null constraint user_service_to_user_fkey references user_account(id) on delete cascade,
    service_id uuid not null constraint user_service_to_service_type_fkey references service_type(id) on delete cascade,
    primary key (user_id, service_id)
);



create table if not exists app_user (
    id bigserial primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    email varchar(255) not null unique,
    role varchar(255) not null,
    created_at timestamp
);

create table if not exists transaction_type (
    id bigserial primary key,
    name varchar(255) not null,
    kind varchar(255) not null,
    user_id bigint not null references app_user(id)
);

create table if not exists app_transaction (
    id bigserial primary key,
    description varchar(255),
    amount numeric(10, 2) not null,
    transaction_kind varchar(255) not null,
    user_id bigint not null references app_user(id),
    transaction_type_id bigint not null references transaction_type(id)
);

create table if not exists forecast_template (
    id bigserial primary key,
    name varchar(255) not null,
    description varchar(255),
    is_public boolean not null default false,
    structure text,
    created_at timestamp
);

create table if not exists forecast_template_owned (
    id bigserial primary key,
    owner_id bigint not null references app_user(id),
    forecast_id bigint not null references forecast_template(id),
    constraint uq_forecast_template_owned unique (owner_id, forecast_id)
);

create table if not exists template_liked (
    id bigserial primary key,
    owner_id bigint not null references app_user(id),
    id_template bigint not null references forecast_template(id)
);

create table if not exists user_follow (
    id bigserial primary key,
    follower_id bigint not null references app_user(id),
    followed_id bigint not null references app_user(id),
    created_at timestamp not null,
    constraint uq_user_follow unique (follower_id, followed_id)
);

create table if not exists genres
(
    id   bigint generated always as identity
        constraint genres_pk primary key,
    name varchar(64) unique not null
);

create table if not exists films
(
    id    bigint generated always as identity
        constraint films_pk primary key,
    name  varchar(256) unique not null,
    genre bigint
        constraint films_genres_null_fk references genres (id)
);

create table if not exists users
(
    id    bigint generated always as identity
        constraint users_pk primary key,
    login varchar(128) unique not null,
    name  varchar(256)
);

create table film_likes
(
    film_id bigint not null
        constraint film_likes_films_null_fk references films (id),
    user_id bigint not null
        constraint film_likes_users_null_fk references users (id),
    constraint film_likes_pk primary key (user_id, film_id)
);


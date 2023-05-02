--liquibase formatted sql

--changeset ks:CRS-2_add_link_sequence

create table if not exists link
(
    id                       serial primary key not null,
    url                      text               not null,
    last_update              timestamp          not null,
    last_edit_time           timestamp          not null,
    count_commit_or_question integer            not null
);
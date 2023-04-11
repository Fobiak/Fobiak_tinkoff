--liquibase formatted sql
--changeset Fobiak:create_link_table
CREATE TABLE if not exists link
(
    id  bigserial primary key,
    url text not null
);
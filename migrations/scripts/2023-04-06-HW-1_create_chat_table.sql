--liquibase formatted sql
--changeset Fobiak:create_chat_table
create table chat
(
    id bigint primary key
)
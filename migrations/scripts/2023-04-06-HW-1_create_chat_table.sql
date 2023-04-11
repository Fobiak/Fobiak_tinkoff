--liquibase formatted sql
--changeset Fobiak:create_chat_table
create table if not exists chat
(
    id bigint primary key
)
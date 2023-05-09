--liquibase formatted sql

--changeset ks:CRS-3_add_userlinks_sequence

create table if not exists user_links
(
    user_id  bigint references client (chat_id),
    links_id bigint references link (id),

    primary key (user_id, links_id)
);
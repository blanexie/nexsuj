create table user_torrent
(
    id          int auto_increment
        primary key,
    info_hash   varchar(100)                       not null,
    user_id     int                                not null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    auth_key    varchar(100)                       not null,
    status      int      default 0                 not null,
    constraint user_torrent_info_hash_IDX
        unique (info_hash, auth_key)
);

create index user_torrent_auth_key_IDX
    on user_torrent (auth_key);

create index user_torrent_user_id_IDX
    on user_torrent (user_id);

INSERT INTO nexusj.user_torrent (id, info_hash, user_id, create_time, auth_key, status) VALUES (1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', 1, '2021-11-13 22:22:19', '60edcd49c33f464bafb7b41db3391ad0', 0);
INSERT INTO nexusj.user_torrent (id, info_hash, user_id, create_time, auth_key, status) VALUES (2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', 2, '2021-11-21 15:17:54', 'c815e9eee6084bfeaf7f7fdac5685272', 0);
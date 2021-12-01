create table user_torrent
(
    id            int auto_increment
        primary key,
    user_id       int                                         not null,
    info_hash     varchar(100)                                not null,
    create_time   datetime        default current_timestamp() not null,
    auth_key      varchar(100)                                not null,
    upload        bigint unsigned default 0                   null,
    download      bigint unsigned default 0                   null,
    real_upload   bigint unsigned default 0                   null,
    real_download bigint unsigned default 0                   null,
    status        int             default 0                   not null,
    constraint user_torrent_info_hash_IDX
        unique (info_hash, auth_key)
);

create index user_torrent_auth_key_IDX
    on user_torrent (auth_key);

create index user_torrent_user_id_IDX
    on user_torrent (user_id);

INSERT INTO nexusj.user_torrent (id, user_id, info_hash, create_time, auth_key, upload, download, real_upload, real_download, status) VALUES (1, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-13 22:22:19', '60edcd49c33f464bafb7b41db3391ad0', 0, 0, 0, 0, 0);
INSERT INTO nexusj.user_torrent (id, user_id, info_hash, create_time, auth_key, upload, download, real_upload, real_download, status) VALUES (2, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 15:17:54', 'c815e9eee6084bfeaf7f7fdac5685272', 0, 0, 0, 0, 0);
INSERT INTO nexusj.user_torrent (id, user_id, info_hash, create_time, auth_key, upload, download, real_upload, real_download, status) VALUES (3, 1, '8f9fd808f68704cffb8fb34ab63e39b70fef8f5a', '2021-11-28 14:39:45', '5a53f836b4274914830667fb75ad8e55', 0, 0, 0, 0, 0);
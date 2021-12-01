create table ud_bytes
(
    id          int auto_increment
        primary key,
    auth_key    varchar(256)             not null,
    info_hash   varchar(256)             not null,
    upload_time datetime                 not null,
    user_id     int                      not null,
    upload      bigint                   not null,
    download    bigint                   not null,
    `left`      bigint                   not null,
    status      varchar(100) default '0' not null
);

create index upbytes_info_hash_IDX
    on ud_bytes (info_hash);

create index upbytes_user_id_IDX
    on ud_bytes (user_id, info_hash);

INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (1, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:43:15', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (2, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:43:52', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (3, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:44:39', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (4, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:46:42', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (5, 'c815e9eee6084bfeaf7f7fdac5685272', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:47:13', 2, 0, 20484550, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (6, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:47:27', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (7, 'c815e9eee6084bfeaf7f7fdac5685272', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 17:47:17', 2, 0, 20484550, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (8, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 17:47:27', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (9, 'c815e9eee6084bfeaf7f7fdac5685272', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 17:47:57', 2, 0, 20484550, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (10, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 18:47:27', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (11, 'c815e9eee6084bfeaf7f7fdac5685272', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 18:48:01', 2, 0, 20484550, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (12, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 19:01:07', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (13, 'c815e9eee6084bfeaf7f7fdac5685272', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 19:48:05', 2, 0, 20484550, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (14, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 20:01:07', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (15, 'c815e9eee6084bfeaf7f7fdac5685272', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 20:29:18', 2, 0, 20484550, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (16, 'c815e9eee6084bfeaf7f7fdac5685272', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 20:50:08', 2, 0, 20484550, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (17, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 21:01:07', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (18, 'c815e9eee6084bfeaf7f7fdac5685272', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 21:50:12', 2, 0, 20484550, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (19, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 22:01:07', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (20, 'c815e9eee6084bfeaf7f7fdac5685272', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 22:50:16', 2, 0, 20484550, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (21, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 23:01:07', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (22, 'c815e9eee6084bfeaf7f7fdac5685272', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 23:50:20', 2, 0, 20484550, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (23, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-22 00:01:07', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (24, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-22 23:33:41', 1, 0, 0, 0, '0');
INSERT INTO nexusj.ud_bytes (id, auth_key, info_hash, upload_time, user_id, upload, download, `left`, status) VALUES (25, '60edcd49c33f464bafb7b41db3391ad0', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-22 23:49:04', 1, 0, 0, 0, '0');
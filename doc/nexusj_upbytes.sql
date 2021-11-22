create table upbytes
(
    id          int auto_increment
        primary key,
    user_id     int                      not null,
    info_hash   varchar(256)             not null,
    upload_time datetime                 not null,
    upload      bigint                   not null,
    download    bigint                   not null,
    `left`      bigint                   not null,
    status      varchar(100) default '0' not null,
    auth_key    varchar(256)             not null
);

create index upbytes_info_hash_IDX
    on upbytes (info_hash);

create index upbytes_user_id_IDX
    on upbytes (user_id, info_hash);

INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (1, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:43:15', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (2, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:43:52', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (3, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:44:39', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (4, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:46:42', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (5, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:47:13', 0, 20484550, 0, '0', 'c815e9eee6084bfeaf7f7fdac5685272');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (6, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 16:47:27', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (7, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 17:47:17', 0, 20484550, 0, '0', 'c815e9eee6084bfeaf7f7fdac5685272');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (8, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 17:47:27', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (9, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 17:47:57', 0, 20484550, 0, '0', 'c815e9eee6084bfeaf7f7fdac5685272');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (10, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 18:47:27', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (11, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 18:48:01', 0, 20484550, 0, '0', 'c815e9eee6084bfeaf7f7fdac5685272');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (12, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 19:01:07', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (13, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 19:48:05', 0, 20484550, 0, '0', 'c815e9eee6084bfeaf7f7fdac5685272');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (14, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 20:01:07', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (15, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 20:29:18', 0, 20484550, 0, '0', 'c815e9eee6084bfeaf7f7fdac5685272');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (16, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 20:50:08', 0, 20484550, 0, '0', 'c815e9eee6084bfeaf7f7fdac5685272');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (17, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 21:01:07', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (18, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 21:50:12', 0, 20484550, 0, '0', 'c815e9eee6084bfeaf7f7fdac5685272');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (19, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 22:01:07', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (20, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 22:50:16', 0, 20484550, 0, '0', 'c815e9eee6084bfeaf7f7fdac5685272');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (21, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 23:01:07', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (22, 2, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-21 23:50:20', 0, 20484550, 0, '0', 'c815e9eee6084bfeaf7f7fdac5685272');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (23, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-22 00:01:07', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (24, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-22 23:33:41', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
INSERT INTO nexusj.upbytes (id, user_id, info_hash, upload_time, upload, download, `left`, status, auth_key) VALUES (25, 1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '2021-11-22 23:49:04', 0, 0, 0, '0', '60edcd49c33f464bafb7b41db3391ad0');
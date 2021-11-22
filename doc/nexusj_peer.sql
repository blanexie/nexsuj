create table peer
(
    id          int auto_increment
        primary key,
    info_hash   varchar(100)                       not null,
    peer_id     varchar(100)                       not null,
    port        int                                not null,
    uploaded    bigint                             not null,
    downloaded  bigint                             not null,
    `left`      bigint                             not null,
    compact     int      default 1                 not null,
    event       varchar(100)                       not null,
    ip          varchar(1024)                      null,
    numwant     int                                null,
    trackerid   varchar(100)                       null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    user_id     int                                not null,
    report_time datetime                           null,
    auth_key    varchar(100)                       not null,
    status      int      default 0                 not null comment '默认0 , 如果-1 就是表示这个peerId被禁用了',
    ipv6        varchar(1024)                      null,
    constraint peer_user_id_info_hash_uindex
        unique (user_id, info_hash)
);

create index peer_auth_key_IDX
    on peer (auth_key);

create index peer_info_hash_IDX
    on peer (info_hash);

create index peer_last_report_time_IDX
    on peer (report_time);

create index peer_peer_id_IDX
    on peer (peer_id);

INSERT INTO nexusj.peer (id, info_hash, peer_id, port, uploaded, downloaded, `left`, compact, event, ip, numwant, trackerid, create_time, user_id, report_time, auth_key, status, ipv6) VALUES (4, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '-qB4370-lpiEobipq9ac', 36333, 0, 20484550, 0, 1, 'completed', '192.168.1.12', 200, null, '2021-11-21 23:50:20', 2, '2021-11-21 23:50:20', 'c815e9eee6084bfeaf7f7fdac5685272', 0, '[
  "2409:8a28:c6c:d80:ba27:ebff:fefc:6eba",
  "2001:0:c38c:c38c:2c40:bbe8:8ff5:fd6b"
]');
INSERT INTO nexusj.peer (id, info_hash, peer_id, port, uploaded, downloaded, `left`, compact, event, ip, numwant, trackerid, create_time, user_id, report_time, auth_key, status, ipv6) VALUES (8, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '-qB4390-wIWeN5E0zy9s', 8110, 0, 0, 0, 1, 'completed', '192.168.1.5', 200, null, '2021-11-22 23:49:04', 1, '2021-11-22 23:49:04', '60edcd49c33f464bafb7b41db3391ad0', 0, '[
  "2409:8a28:c6c:d80:3881:e173:5a18:3a79",
  "2409:8a28:c6c:d80:a042:d29b:455:9b09"
]');
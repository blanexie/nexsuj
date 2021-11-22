create table torrent
(
    id            int auto_increment
        primary key,
    announce      varchar(100)                       not null,
    created_by    varchar(100)                       null,
    comment       varchar(1024)                      null,
    creation_date bigint                             null,
    encoding      varchar(100)                       null,
    info_hash     varchar(100)                       not null,
    name          varchar(100)                       null,
    private       int      default 1                 not null,
    files         json                               null,
    size          bigint                             not null,
    user_id       int                                not null,
    status        int      default 0                 not null,
    upload_time   datetime default CURRENT_TIMESTAMP not null,
    type          varchar(100)                       not null,
    labels        json                               not null,
    title         varchar(100)                       not null,
    ratio_up      float    default 1                 not null,
    ratio_down    float    default 1                 not null,
    ration_time   datetime                           null
);

create index torrent_info_hash_IDX
    on torrent (info_hash);

create index torrent_name_IDX
    on torrent (name);

create index torrent_title_IDX
    on torrent (title);

create index torrent_user_id_IDX
    on torrent (user_id);

INSERT INTO nexusj.torrent (id, announce, created_by, comment, creation_date, encoding, info_hash, name, private, files, size, user_id, status, upload_time, type, labels, title, ratio_up, ratio_down, ration_time) VALUES (1, 'http://192.168.0.112:8080/announce', 'qBittorrent v4.3.8', '', 1632934131, '', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '重学Java设计模式·小傅哥(公众号：bugstack虫洞栈).pdf', 1, null, 20484550, 1, 0, '2021-11-13 22:21:35', '分类', '["标签"]', '标题', 1, 1, null);
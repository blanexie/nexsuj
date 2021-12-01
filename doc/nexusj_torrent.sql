create table torrent
(
    id          int auto_increment
        primary key,
    info_hash   varchar(100)                       not null,
    title       varchar(100)                       not null,
    size        bigint                             not null,
    type        varchar(100)                       not null,
    labels      json                               not null,
    cover_path  varchar(100)                       null,
    img_list    json                               null,
    description longtext                           null,
    files       json                               null,
    upload_time datetime default CURRENT_TIMESTAMP not null,
    status      int      default 0                 not null,
    ratio       int      default 1                 not null,
    ration_time datetime                           null
);

create index torrent_info_hash_IDX
    on torrent (info_hash);

create index torrent_title_IDX
    on torrent (title);

INSERT INTO nexusj.torrent (id, info_hash, title, size, type, labels, cover_path, img_list, description, files, upload_time, status, ratio, ration_time) VALUES (1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', '标题', 20484550, '分类', '["标签"]', 'https://www.wahaotu.com/uploads/allimg/201904/1555074510295049.jpg', null, 'qBittorrent v4.3.8', '[{"path": ["SE5ELTY5NyDnpZ7lkrLoqannuZQg44GZ44KT44GU44GE5Lmz6aaW6LKs44KB44Gn5Lit5Ye644GX44KS6KqY44GG6YCj57aa6Iaj5pC+44KK55e05aWz44GK5aeJ44GV44KTLmpwZw=="], "length": 181472}, {"path": ["SE5ELTY5Ny5tcDQ="], "length": 591776346}]', '2021-11-13 22:21:35', 0, 1, '2021-11-28 14:38:24');
INSERT INTO nexusj.torrent (id, info_hash, title, size, type, labels, cover_path, img_list, description, files, upload_time, status, ratio, ration_time) VALUES (2, '8f9fd808f68704cffb8fb34ab63e39b70fef8f5a', '标题', 592445440, '分类', '["标签"]', 'https://www.wahaotu.com/uploads/allimg/201904/1555074510295049.jpg', null, '描述', '[{"path": ["SE5ELTY5NyDnpZ7lkrLoqannuZQg44GZ44KT44GU44GE5Lmz6aaW6LKs44KB44Gn5Lit5Ye644GX44KS6KqY44GG6YCj57aa6Iaj5pC+44KK55e05aWz44GK5aeJ44GV44KTLmpwZw=="], "length": 181472}, {"path": ["SE5ELTY5Ny5tcDQ="], "length": 591776346}]', '2021-11-28 14:38:24', 0, 1, '2021-11-28 14:38:24');
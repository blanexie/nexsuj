create table torrent
(
    id          int auto_increment
        primary key,
    info_hash   varchar(100)                              not null,
    name        varchar(255)                              not null comment '种子中文件的名称',
    title       varchar(100)                              not null,
    size        bigint                                    not null,
    type        varchar(100)                              not null,
    labels      longtext collate utf8mb4_bin              not null,
    img_list    varchar(1024) default '[]'                not null,
    cover_path  varchar(100)                              null,
    description longtext                                  null,
    files       longtext collate utf8mb4_bin              null,
    upload_time datetime      default current_timestamp() not null,
    status      int           default 0                   not null,
    ration      int           default 1                   not null,
    ration_time datetime                                  null,
    constraint files
        check (json_valid(`files`)),
    constraint labels
        check (json_valid(`labels`))
);

create index torrent_info_hash_IDX
    on torrent (info_hash);

create index torrent_title_IDX
    on torrent (title);

INSERT INTO nexusj.torrent (id, info_hash, name, title, size, type, labels, img_list, cover_path, description, files, upload_time, status, ration, ration_time) VALUES (1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', 'The.Mindy.Project.S01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR', '标题', 20484550, '分类', '["标签"]', '[]', 'https://www.wahaotu.com/uploads/allimg/201904/1555074510295049.jpg', 'qBittorrent v4.3.8', '[{"path": ["SE5ELTY5NyDnpZ7lkrLoqannuZQg44GZ44KT44GU44GE5Lmz6aaW6LKs44KB44Gn5Lit5Ye644GX44KS6KqY44GG6YCj57aa6Iaj5pC+44KK55e05aWz44GK5aeJ44GV44KTLmpwZw=="], "length": 181472}, {"path": ["SE5ELTY5Ny5tcDQ="], "length": 591776346}]', '2021-11-13 22:21:35', 0, 1, '2021-11-28 14:38:24');
INSERT INTO nexusj.torrent (id, info_hash, name, title, size, type, labels, img_list, cover_path, description, files, upload_time, status, ration, ration_time) VALUES (2, '8f9fd808f68704cffb8fb34ab63e39b70fef8f5a', 'The.Mindy.Project.S01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR', '标题', 592445440, '分类', '["标签"]', '[]', 'https://www.wahaotu.com/uploads/allimg/201904/1555074510295049.jpg', '描述', '[{"path": ["SE5ELTY5NyDnpZ7lkrLoqannuZQg44GZ44KT44GU44GE5Lmz6aaW6LKs44KB44Gn5Lit5Ye644GX44KS6KqY44GG6YCj57aa6Iaj5pC+44KK55e05aWz44GK5aeJ44GV44KTLmpwZw=="], "length": 181472}, {"path": ["SE5ELTY5Ny5tcDQ="], "length": 591776346}]', '2021-11-28 14:38:24', 0, 1, '2021-11-28 14:38:24');
INSERT INTO nexusj.torrent (id, info_hash, name, title, size, type, labels, img_list, cover_path, description, files, upload_time, status, ration, ration_time) VALUES (3, '267d381fd7882dd40cdcd2b0c092577bc81d7972', 'The.Mindy.Project.S01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR', '标题', 95227478016, '分类', '["标签"]', '["C:\\\\Users\\\\MI\\\\AppData\\\\Local\\\\Temp\\\\/c26c1d81306f490299db801cf10a0f6d.jpeg"]', 'C:\\Users\\MI\\AppData\\Local\\Temp\\/62309366dc2b40d3ae3ba664a500aa99.jpeg', '描述', '[{"length":1772,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00000.clpi"]},{"length":1672,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00001.clpi"]},{"length":652,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00002.clpi"]},{"length":656,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00003.clpi"]},{"length":1296,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00004.clpi"]},{"length":624,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00005.clpi"]},{"length":432,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00006.clpi"]},{"length":352,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00007.clpi"]},{"length":496,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00008.clpi"]},{"length":352,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00009.clpi"]},{"length":9940,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00010.clpi"]},{"length":9480,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00011.clpi"]},{"length":9656,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00012.clpi"]},{"length":9388,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00013.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00014.clpi"]},{"length":9480,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00015.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00016.clpi"]},{"length":9676,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00017.clpi"]},{"length":9664,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00018.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00019.clpi"]},{"length":9508,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00020.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00021.clpi"]},{"length":9624,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00022.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00023.clpi"]},{"length":9724,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00024.clpi"]},{"length":9360,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00025.clpi"]},{"length":9560,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00026.clpi"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00000.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00001.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00002.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00003.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00004.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00005.mpls"]},{"length":168,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00006.mpls"]},{"length":170,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00007.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00008.mpls"]},{"length":170,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00009.mpls"]},{"length":256,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00010.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00011.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00012.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00013.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00014.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00015.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00016.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00017.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00018.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00019.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00020.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00021.mpls"]},{"length":1124,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00000.clpi"]},{"length":612,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00001.clpi"]},{"length":352,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00002.clpi"]},{"length":352,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00003.clpi"]},{"length":432,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00004.clpi"]},{"length":496,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00005.clpi"]},{"length":9752,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00006.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00007.clpi"]},{"length":9456,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00008.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00009.clpi"]},{"length":9200,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00010.clpi"]},{"length":9664,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00011.clpi"]},{"length":9412,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00012.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00013.clpi"]},{"length":9752,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00014.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00015.clpi"]},{"length":9800,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00016.clpi"]},{"length":9836,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00017.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00018.clpi"]},{"length":9608,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00019.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00020.clpi"]},{"length":9696,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00021.clpi"]},{"length":9996,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00022.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00023.clpi"]},{"length":9708,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00024.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00025.clpi"]},{"length":908,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","CLIPINF","00026.clpi"]},{"length":184,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00000.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00001.mpls"]},{"length":170,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00002.mpls"]},{"length":170,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00003.mpls"]},{"length":168,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00004.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00005.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00006.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00007.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00008.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00009.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00010.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00011.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00012.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00013.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00014.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00015.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00016.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00017.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","PLAYLIST","00018.mpls"]},{"length":540,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","index.bdmv"]},{"length":5546,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","MovieObject.bdmv"]},{"length":1772,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00000.clpi"]},{"length":1672,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00001.clpi"]},{"length":652,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00002.clpi"]},{"length":656,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00003.clpi"]},{"length":1296,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00004.clpi"]},{"length":624,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00005.clpi"]},{"length":432,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00006.clpi"]},{"length":352,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00007.clpi"]},{"length":496,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00008.clpi"]},{"length":352,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00009.clpi"]},{"length":9940,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00010.clpi"]},{"length":9480,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00011.clpi"]},{"length":9656,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00012.clpi"]},{"length":9388,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00013.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00014.clpi"]},{"length":9480,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00015.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00016.clpi"]},{"length":9676,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00017.clpi"]},{"length":9664,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00018.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00019.clpi"]},{"length":9508,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00020.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00021.clpi"]},{"length":9624,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00022.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00023.clpi"]},{"length":9724,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00024.clpi"]},{"length":9360,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00025.clpi"]},{"length":9560,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00026.clpi"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00000.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00001.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00002.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00003.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00004.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00005.mpls"]},{"length":168,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00006.mpls"]},{"length":170,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00007.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00008.mpls"]},{"length":170,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00009.mpls"]},{"length":256,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00010.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00011.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00012.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00013.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00014.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00015.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00016.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00017.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00018.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00019.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00020.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00021.mpls"]},{"length":331677696,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00000.m2ts"]},{"length":318898176,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00001.m2ts"]},{"length":69206016,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00002.m2ts"]},{"length":71172096,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00003.m2ts"]},{"length":227082240,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00004.m2ts"]},{"length":62521344,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00005.m2ts"]},{"length":35389440,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00006.m2ts"]},{"length":786432,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00007.m2ts"]},{"length":55443456,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00008.m2ts"]},{"length":786432,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00009.m2ts"]},{"length":3973054464,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00010.m2ts"]},{"length":3946512384,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00011.m2ts"]},{"length":4032430080,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00012.m2ts"]},{"length":3798663168,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00013.m2ts"]},{"length":393216,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00014.m2ts"]},{"length":3870228480,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00015.m2ts"]},{"length":393216,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00016.m2ts"]},{"length":4011786240,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00017.m2ts"]},{"length":3955359744,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00018.m2ts"]},{"length":393216,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00019.m2ts"]},{"length":3910729728,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00020.m2ts"]},{"length":393216,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00021.m2ts"]},{"length":4070572032,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00022.m2ts"]},{"length":393216,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00023.m2ts"]},{"length":3814981632,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00024.m2ts"]},{"length":3734372352,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00025.m2ts"]},{"length":3944546304,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00026.m2ts"]},{"length":104,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","CERTIFICATE","BACKUP","id.bdmv"]},{"length":504,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","index.bdmv"]},{"length":4886,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","BACKUP","MovieObject.bdmv"]},{"length":1124,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00000.clpi"]},{"length":612,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00001.clpi"]},{"length":352,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00002.clpi"]},{"length":352,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00003.clpi"]},{"length":432,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00004.clpi"]},{"length":496,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00005.clpi"]},{"length":9752,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00006.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00007.clpi"]},{"length":9456,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00008.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00009.clpi"]},{"length":9200,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00010.clpi"]},{"length":9664,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00011.clpi"]},{"length":9412,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00012.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00013.clpi"]},{"length":9752,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00014.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00015.clpi"]},{"length":9800,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00016.clpi"]},{"length":9836,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00017.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00018.clpi"]},{"length":9608,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00019.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00020.clpi"]},{"length":9696,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00021.clpi"]},{"length":9996,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00022.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00023.clpi"]},{"length":9708,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00024.clpi"]},{"length":376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00025.clpi"]},{"length":908,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","CLIPINF","00026.clpi"]},{"length":184,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00000.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00001.mpls"]},{"length":170,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00002.mpls"]},{"length":170,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00003.mpls"]},{"length":168,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00004.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00005.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00006.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00007.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00008.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00009.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00010.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00011.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00012.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00013.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00014.mpls"]},{"length":270,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00015.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00016.mpls"]},{"length":354,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00017.mpls"]},{"length":184,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","PLAYLIST","00018.mpls"]},{"length":175964160,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00000.m2ts"]},{"length":54460416,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00001.m2ts"]},{"length":786432,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00002.m2ts"]},{"length":786432,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00003.m2ts"]},{"length":35389440,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00004.m2ts"]},{"length":55443456,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00005.m2ts"]},{"length":3862757376,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00006.m2ts"]},{"length":196608,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00007.m2ts"]},{"length":3830710272,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00008.m2ts"]},{"length":196608,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00009.m2ts"]},{"length":3573350400,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00010.m2ts"]},{"length":3833856000,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00011.m2ts"]},{"length":3683057664,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00012.m2ts"]},{"length":393216,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00013.m2ts"]},{"length":4002349056,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00014.m2ts"]},{"length":393216,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00015.m2ts"]},{"length":4155506688,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00016.m2ts"]},{"length":4021420032,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00017.m2ts"]},{"length":196608,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00018.m2ts"]},{"length":3814391808,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00019.m2ts"]},{"length":196608,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00020.m2ts"]},{"length":3792961536,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00021.m2ts"]},{"length":3911319552,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00022.m2ts"]},{"length":393216,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00023.m2ts"]},{"length":4054646784,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00024.m2ts"]},{"length":393216,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00025.m2ts"]},{"length":125632512,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","STREAM","00026.m2ts"]},{"length":104,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","CERTIFICATE","BACKUP","id.bdmv"]},{"length":540,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","index.bdmv"]},{"length":5546,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","MovieObject.bdmv"]},{"length":104,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","CERTIFICATE","id.bdmv"]},{"length":504,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","index.bdmv"]},{"length":4886,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","BDMV","MovieObject.bdmv"]},{"length":104,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","CERTIFICATE","id.bdmv"]},{"length":152,"path":["The.Mindy.Project.S01D01.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","disc.inf"]},{"length":152,"path":["The.Mindy.Project.S01D02.1080p.Blu-ray.AVC.DTS-HD.MA.5.1-SbR","disc.inf"]}]', '2021-12-01 23:46:38', 0, 1, null);
INSERT INTO nexusj.torrent (id, info_hash, name, title, size, type, labels, img_list, cover_path, description, files, upload_time, status, ration, ration_time) VALUES (5, '8b6e4a1bcc22e7a6241c0547b6a2c25a00fa01c0', '屏幕快照', '标题', 491520, '分类', '["标签"]', '["339ef91f68e44ced8cf8f4e1cdae308f.jpeg"]', '46da63131bd243cfa4f3db4f8f555c6c.jpeg', '描述', '[{"length":477984,"path":["屏幕截图(1).png"]}]', '2021-12-02 00:00:06', 0, 1, '2021-12-05 00:00:07');
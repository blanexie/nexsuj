create table user
(
    id            int auto_increment
        primary key,
    email         varchar(100)                                not null,
    pwd           varchar(100)                                not null,
    nick          varchar(100)                                null,
    avatar        varchar(256)                                null comment '头像',
    sex           int                                         null,
    role_id       int             default 1                   null,
    upload        bigint unsigned default 0                   not null,
    download      bigint unsigned default 0                   not null,
    real_upload   bigint unsigned default 0                   null,
    real_download bigint unsigned default 0                   null,
    integral      bigint          default 0                   not null,
    create_time   datetime        default current_timestamp() not null,
    update_time   datetime        default current_timestamp() not null,
    status        int             default 0                   not null comment '* 0: 正常用户 ',
    constraint user_email_uindex
        unique (email)
);

create index user_email_IDX
    on user (email);

create index user_nick_IDX
    on user (nick);

INSERT INTO nexusj.user (id, email, pwd, nick, avatar, sex, role_id, upload, download, real_upload, real_download, integral, create_time, update_time, status) VALUES (1, 'abc@qq.com', '123456', 'abc', 'https://www.wahaotu.com/uploads/allimg/201904/1555074510295049.jpg', 1, 1, 0, 0, 0, 0, 0, '2021-11-11 17:02:16', '2021-11-11 17:02:16', 0);
INSERT INTO nexusj.user (id, email, pwd, nick, avatar, sex, role_id, upload, download, real_upload, real_download, integral, create_time, update_time, status) VALUES (2, 'abd@qq.com', '123456', 'abd', 'https://www.wahaotu.com/uploads/allimg/201904/1555074510295049.jpg', 1, 1, 0, 0, 0, 0, 0, '2021-11-21 15:14:31', '2021-11-11 17:02:16', 0);
INSERT INTO nexusj.user (id, email, pwd, nick, avatar, sex, role_id, upload, download, real_upload, real_download, integral, create_time, update_time, status) VALUES (4, 'abe@qq.com', '123456', 'abe', 'https://www.wahaotu.com/uploads/allimg/201904/1555074510295049.jpg', 1, 1, 0, 0, 0, 0, 0, '2021-11-26 23:20:53', '2021-11-26 23:20:53', 0);
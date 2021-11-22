create table user
(
    id          int auto_increment
        primary key,
    email       varchar(100)                              not null,
    pwd         varchar(100)                              not null,
    sex         int                                       null,
    role_id     int             default 1                 null,
    nick        varchar(100)                              null,
    upload      bigint unsigned default '0'               not null,
    download    bigint unsigned default '0'               not null,
    integral    bigint          default 0                 not null,
    create_time datetime        default CURRENT_TIMESTAMP not null,
    update_time datetime        default CURRENT_TIMESTAMP not null,
    auth_key    varchar(100)                              not null,
    status      int             default 0                 not null comment '* 0: 正常用户',
    unlock_time datetime                                  null,
    avatar      varchar(256)                              null comment '头像'
);

create index user_auth_key_IDX
    on user (auth_key);

create index user_email_IDX
    on user (email);

create index user_nick_IDX
    on user (nick);

INSERT INTO nexusj.user (id, email, pwd, sex, role_id, nick, upload, download, integral, create_time, update_time, auth_key, status, unlock_time, avatar) VALUES (1, 'abc@qq.com', '123456', 1, 1, 'abc', 0, 0, 0, '2021-11-11 17:02:16', '2021-11-11 17:02:16', 'e934b04ba3234d639c0523f0cb15fe0d', 0, null, null);
INSERT INTO nexusj.user (id, email, pwd, sex, role_id, nick, upload, download, integral, create_time, update_time, auth_key, status, unlock_time, avatar) VALUES (2, 'abd@qq.com', '123456', 1, 1, 'abd', 0, 0, 0, '2021-11-21 15:14:31', '2021-11-11 17:02:16', 'e934b04ba3234d639c0523f0cb15fe01', 0, null, null);
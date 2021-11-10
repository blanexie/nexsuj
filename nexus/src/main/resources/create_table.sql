

-- docker run -p 8306:3306 --name  mysql -v /home/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:8.0.27


create table peer
(
    id int auto_increment
        primary key,
    info_hash varchar(100) not null,
    peer_id varchar(100) not null,
    port int not null,
    uploaded bigint not null,
    downloaded bigint not null,
    `left` bigint not null,
    compact int default 1 not null,
    event varchar(100) not null,
    ip varchar(100) not null,
    numwant int null,
    trackerid varchar(100) null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    user_id int not null,
    last_report_time datetime null,
    auth_key varchar(100) not null,
    status int default 0 not null comment '默认0 , 如果-1 就是表示这个peerId被禁用了'
);

create index peer_auth_key_IDX
	on peer (auth_key);

create index peer_info_hash_IDX
	on peer (info_hash);

create index peer_last_report_time_IDX
	on peer (last_report_time);

create index peer_peer_id_IDX
	on peer (peer_id);

create index peer_user_id_IDX
	on peer (user_id);

create table torrent
(
    id int auto_increment
        primary key,
    announce varchar(100) not null,
    created_by varchar(100) null,
    comment varchar(1024) null,
    creation_date bigint null,
    encoding varchar(100) null,
    info_hash varchar(100) not null,
    name varchar(100) null,
    private int default 1 not null,
    files json null,
    size bigint not null,
    user_id int not null,
    status int default 0 not null,
    upload_time datetime default CURRENT_TIMESTAMP not null,
    type varchar(100) not null,
    labels json not null,
    title varchar(100) not null,
    ratio_up float default 1 not null,
    ratio_down float default 1 not null,
    ration_time datetime null
);

create index torrent_info_hash_IDX
	on torrent (info_hash);

create index torrent_name_IDX
	on torrent (name);

create index torrent_title_IDX
	on torrent (title);

create index torrent_user_id_IDX
	on torrent (user_id);

create table torrent_info
(
    info_hash varchar(100) not null
        primary key,
    info blob not null,
    description text not null
);

create table upbytes
(
    id int auto_increment
        primary key,
    user_id int not null,
    info_hash varchar(256) not null,
    upload_time datetime not null,
    upload bigint not null,
    download bigint not null,
    `left` bigint not null,
    change_upload int null,
    change_download int null,
    change_integral int null,
    status varchar(100) default '0' not null
);

create index upbytes_info_hash_IDX
	on upbytes (info_hash);

create index upbytes_user_id_IDX
	on upbytes (user_id, info_hash);

create table user
(
    id int auto_increment
        primary key,
    email varchar(100) not null,
    pwd varchar(100) not null,
    sex int null,
    nick varchar(100) null,
    upload bigint unsigned default '0' not null,
    download bigint unsigned default '0' not null,
    integral bigint default 0 not null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null,
    auth_key varchar(100) not null,
    status int default 0 not null comment '* 0: 正常用户',
    unlock_time datetime null
);

create index user_auth_key_IDX
	on user (auth_key);

create index user_email_IDX
	on user (email);

create index user_nick_IDX
	on user (nick);

create table user_torrent
(
    id int auto_increment
        primary key,
    info_hash varchar(100) not null,
    user_id int not null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    auth_key varchar(100) not null,
    status int default 0 not null,
    constraint user_torrent_info_hash_IDX
        unique (info_hash, auth_key)
);

create index user_torrent_auth_key_IDX
	on user_torrent (auth_key);

create index user_torrent_user_id_IDX
	on user_torrent (user_id);


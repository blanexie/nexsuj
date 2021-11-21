

-- docker run -p 8306:3306 --name  mysql -v /home/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:8.0.27
--  docker run -p 8306:3306 --name  mysql -v C://home/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:8.0.27
CREATE DATABASE `nexusj` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;


-- nexusj.peer definition

CREATE TABLE `peer` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `info_hash` varchar(100) NOT NULL,
                        `peer_id` varchar(100) NOT NULL,
                        `port` int NOT NULL,
                        `uploaded` bigint NOT NULL,
                        `downloaded` bigint NOT NULL,
                        `left` bigint NOT NULL,
                        `compact` int NOT NULL DEFAULT '1',
                        `event` varchar(100) NOT NULL,
                        `ip` varchar(1024) DEFAULT NULL,
                        `ipv6` varchar(1024) DEFAULT NULL,
                        `numwant` int DEFAULT NULL,
                        `trackerid` varchar(100) DEFAULT NULL,
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `user_id` int NOT NULL,
                        `report_time` datetime DEFAULT NULL,
                        `auth_key` varchar(100) NOT NULL,
                        `status` int NOT NULL DEFAULT '0' COMMENT '默认0 , 如果-1 就是表示这个peerId被禁用了',
                        PRIMARY KEY (`id`),
                        KEY `peer_auth_key_IDX` (`auth_key`),
                        KEY `peer_info_hash_IDX` (`info_hash`),
                        KEY `peer_last_report_time_IDX` (`report_time`),
                        KEY `peer_peer_id_IDX` (`peer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
create unique index peer_user_id_info_hash_uindex
    on peer (user_id, info_hash);


-- nexusj.torrent definition

CREATE TABLE `torrent` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `announce` varchar(100) NOT NULL,
                           `created_by` varchar(100) DEFAULT NULL,
                           `comment` varchar(1024) DEFAULT NULL,
                           `creation_date` bigint DEFAULT NULL,
                           `encoding` varchar(100) DEFAULT NULL,
                           `info_hash` varchar(100) NOT NULL,
                           `name` varchar(100) DEFAULT NULL,
                           `private` int NOT NULL DEFAULT '1',
                           `files` json DEFAULT NULL,
                           `size` bigint NOT NULL,
                           `user_id` int NOT NULL,
                           `status` int NOT NULL DEFAULT '0',
                           `upload_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `type` varchar(100) NOT NULL,
                           `labels` json NOT NULL,
                           `title` varchar(100) NOT NULL,
                           `ratio_up` float NOT NULL DEFAULT '1',
                           `ratio_down` float NOT NULL DEFAULT '1',
                           `ration_time` datetime DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `torrent_info_hash_IDX` (`info_hash`),
                           KEY `torrent_name_IDX` (`name`),
                           KEY `torrent_title_IDX` (`title`),
                           KEY `torrent_user_id_IDX` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- nexusj.info definition

CREATE TABLE `info` (
                        `info_hash` varchar(100) NOT NULL,
                        `info` blob NOT NULL,
                        `description` text NOT NULL,
                        PRIMARY KEY (`info_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- nexusj.upbytes definition

CREATE TABLE `upbytes` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `user_id` int NOT NULL,
                           `info_hash` varchar(256) NOT NULL,
                           `upload_time` datetime NOT NULL,
                           `upload` bigint NOT NULL,
                           `download` bigint NOT NULL,
                           `left` bigint NOT NULL,
                           `status` varchar(100) NOT NULL DEFAULT '0',
                           PRIMARY KEY (`id`),
                           KEY `upbytes_info_hash_IDX` (`info_hash`),
                           KEY `upbytes_user_id_IDX` (`user_id`,`info_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- nexusj.`user` definition

CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `email` varchar(100) NOT NULL,
                        `pwd` varchar(100) NOT NULL,
                        `sex` int DEFAULT NULL,
                        `role_id` int DEFAULT 1,
                        `nick` varchar(100) DEFAULT NULL,
                        `upload` bigint unsigned NOT NULL DEFAULT '0',
                        `download` bigint unsigned NOT NULL DEFAULT '0',
                        `integral` bigint NOT NULL DEFAULT '0',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `auth_key` varchar(100) NOT NULL,
                        `status` int NOT NULL DEFAULT '0' COMMENT '* 0: 正常用户',
                        `unlock_time` datetime DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `user_auth_key_IDX` (`auth_key`),
                        KEY `user_email_IDX` (`email`),
                        KEY `user_nick_IDX` (`nick`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- nexusj.user_torrent definition

CREATE TABLE `user_torrent` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `info_hash` varchar(100) NOT NULL,
                                `user_id` int NOT NULL,
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `auth_key` varchar(100) NOT NULL,
                                `status` int NOT NULL DEFAULT '0',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `user_torrent_info_hash_IDX` (`info_hash`,`auth_key`),
                                KEY `user_torrent_auth_key_IDX` (`auth_key`),
                                KEY `user_torrent_user_id_IDX` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO nexusj.user (id, email, pwd, sex, nick, upload, download, integral, create_time, update_time, auth_key, status, unlock_time)
VALUES (1, 'abc@qq.com', '123456', 1, 'abc', 0, 0, 0, '2021-11-11 17:02:16', '2021-11-11 17:02:16', 'e934b04ba3234d639c0523f0cb15fe0d', 0, null);

INSERT INTO nexusj.torrent (id, announce, created_by, comment, creation_date, encoding, info_hash, name, private, files, size, user_id, status, upload_time, type, labels, title, ratio_up, ratio_down, ration_time)
 VALUES (1, 'http://192.168.0.112:8080/announce', 'qBittorrent v4.3.8', '', 1632934131, '', '6a0736e2a428c9151f8157a54c81c9107b393ac6', '重学Java设计模式·小傅哥(公众号：bugstack虫洞栈).pdf', 1, null, 20484550, 1, 0, '2021-11-13 22:21:35', '分类', '["标签"]', '标题', 1, 1, null);

INSERT INTO nexusj.info (info_hash, info, description)
VALUES ('6a0736e2a428c9151f8157a54c81c9107b393ac6', 0x64363A6C656E67746869323034383435353065343A6E616D6536383AE9878DE5ADA64A617661E8AEBEE8AEA1E6A8A1E5BC8FC2B7E5B08FE58285E593A528E585ACE4BC97E58FB7EFBC9A627567737461636BE899ABE6B49EE6A088292E70646631323A7069656365206C656E677468693130343835373665363A7069656365733430303A0E564F81187D8F943FDD1DC0E3695181DE30AE592832B7EB42C948770D8FC53BB7DCE8A5BBD4891D3067EE001DA92C4EA0B075D76D9A639CF6C070C943880DDC25477057B23736C299E47571329E4A45110C9FF3EE97CF54607A221625DC2F25B48E13ADA2969AB6052BAAC149CF4A8302AE55A138B916142A050B004A5AFB02409566BE62DF1DC80F729DB164D86B81D6527139A429781699F0B359BC7F7E6129ED6223891AE2EDF10AF72C6E9F0061C532DDE1F4A53B261FD6C23E2672A3C1274F5DD8C98961F901E08DB94917CBA64658275961FD90FCD97C7E323F0A781DBAAACFEA91117BD2CCC2FE0944BCCCF8DDEB7E086B47EEB70A6D04BF67C9A1622E1F3B3612D54D23B1DF759410FA537C2DE63848EF37B689A8005F1E3554A07388A0B0A9263E8003A55B4DE880E999FF03F70F59CD08694CAE9FE4635069F2BAD240ACB56D067400724D2C994A082644A8455C377792704B3471712686C418A59C6909A42697E6A31A1B3043F8E1E5C0A8160F2AF7946864FF68063E45610A1A92CE2F8E296E1F36B6F79F5E64B7E23F373A7072697661746569316565, '描述');

INSERT INTO nexusj.user_torrent (id, info_hash, user_id, create_time, auth_key, status)
VALUES (1, '6a0736e2a428c9151f8157a54c81c9107b393ac6', 1, '2021-11-13 22:22:19', '60edcd49c33f464bafb7b41db3391ad0', 0);
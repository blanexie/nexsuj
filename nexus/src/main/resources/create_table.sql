-- nexus.upbytes definition

CREATE TABLE `upbytes` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `user_id` int NOT NULL,
                           `info_hash` varchar(256) NOT NULL,
                           `upload_time` datetime NOT NULL,
                           `upload` bigint NOT NULL,
                           `download` bigint NOT NULL,
                           `left` bigint NOT NULL,
                           `change_upload` int DEFAULT NULL,
                           `change_download` int DEFAULT NULL,
                           `change_integral` int DEFAULT NULL,
                           `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
                           PRIMARY KEY (`id`),
                           KEY `upbytes_user_id_IDX` (`user_id`,`info_hash`) USING BTREE,
                           KEY `upbytes_info_hash_IDX` (`info_hash`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- nexus.`user` definition

CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `email` varchar(100) NOT NULL,
                        `pwd` varchar(100) NOT NULL,
                        `sex` int DEFAULT NULL,
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
                        KEY `user_email_IDX` (`email`) USING BTREE,
                        KEY `user_nick_IDX` (`nick`) USING BTREE,
                        KEY `user_auth_key_IDX` (`auth_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- nexus.user_torrent definition

CREATE TABLE `user_torrent` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `info_hash` varchar(100) NOT NULL,
                                `user_id` int NOT NULL,
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `auth_key` varchar(100) NOT NULL,
                                `status` int NOT NULL DEFAULT '0',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `user_torrent_info_hash_IDX` (`info_hash`,`auth_key`) USING BTREE,
                                KEY `user_torrent_auth_key_IDX` (`auth_key`) USING BTREE,
                                KEY `user_torrent_user_id_IDX` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- nexus.peer definition

CREATE TABLE `peer` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `info_hash` varchar(100) NOT NULL,
                        `peer_id` varchar(100) NOT NULL,
                        `port` int NOT NULL,
                        `uploaded` bigint NOT NULL,
                        `downloaded` bigint NOT NULL,
                        `left` bigint NOT NULL,
                        `compact` int NOT NULL DEFAULT '1',
                        `event` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                        `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                        `numwant` int DEFAULT NULL,
                        `trackerid` varchar(100) DEFAULT NULL,
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `user_id` int NOT NULL,
                        `last_report_time` datetime DEFAULT NULL,
                        `auth_key` varchar(100) NOT NULL,
                        `status` int NOT NULL DEFAULT '0' COMMENT '默认0 , 如果-1 就是表示这个peerId被禁用了',
                        PRIMARY KEY (`id`),
                        KEY `peer_info_hash_IDX` (`info_hash`) USING BTREE,
                        KEY `peer_peer_id_IDX` (`peer_id`) USING BTREE,
                        KEY `peer_auth_key_IDX` (`auth_key`) USING BTREE,
                        KEY `peer_last_report_time_IDX` (`last_report_time`) USING BTREE,
                        KEY `peer_user_id_IDX` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- nexus.torrent definition

CREATE TABLE `torrent` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `announce` varchar(100) NOT NULL,
                           `created_by` varchar(100) DEFAULT NULL,
                           `comment` varchar(1024) DEFAULT NULL,
                           `creation_date` datetime DEFAULT NULL,
                           `encoding` varchar(100) DEFAULT NULL,
                           `info_hash` varchar(100) NOT NULL,
                           `name` varchar(100) DEFAULT NULL,
                           `private` int NOT NULL DEFAULT '1',
                           `files` json NOT NULL,
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
                           KEY `torrent_info_hash_IDX` (`info_hash`) USING BTREE,
                           KEY `torrent_name_IDX` (`name`) USING BTREE,
                           KEY `torrent_title_IDX` (`title`) USING BTREE,
                           KEY `torrent_user_id_IDX` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- nexus.torrent_info definition

CREATE TABLE `torrent_info` (
                                `info_hash` varchar(100) NOT NULL,
                                `info` blob NOT NULL,
                                `description` text NOT NULL,
                                PRIMARY KEY (`info_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
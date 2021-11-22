create table role
(
    id         int         null,
    name       varchar(64) null comment '角色名',
    properties json        null
);

INSERT INTO nexusj.role (id, name, properties) VALUES (1, '管理员', '{}');
INSERT INTO nexusj.role (id, name, properties) VALUES (2, '普通用户', '{}');
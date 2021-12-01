create table role
(
    id        int         null,
    name      varchar(64) null comment '角色名',
    attribute json        null
);

INSERT INTO nexusj.role (id, name, attribute) VALUES (1, '管理员', '{}');
INSERT INTO nexusj.role (id, name, attribute) VALUES (2, '普通用户', '{}');
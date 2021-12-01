create table role
(
    id        int                          null,
    name      varchar(64)                  null comment '角色名',
    attribute longtext collate utf8mb4_bin null,
    constraint attribute
        check (json_valid(`attribute`))
);

INSERT INTO nexusj.role (id, name, attribute) VALUES (1, '管理员', '{}');
INSERT INTO nexusj.role (id, name, attribute) VALUES (2, '普通用户', '{}');
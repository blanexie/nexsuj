create table attribute
(
    id    int auto_increment
        primary key,
    type  varchar(24) not null,
    name  varchar(24) not null,
    value text        null
);


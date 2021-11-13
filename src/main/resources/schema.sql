create table user
(
    user_id            BINARY(16) not null PRIMARY KEY,
    birth_date         varchar(255) not null,
    name               varchar(255) not null,
    username           varchar(255) not null,
    password           varchar(255) not null,
    college            varchar(255),
    is_graduated       boolean,
    wanted_job         varchar(255) not null,
    job                varchar(255),
    created_date       timestamp,
    last_modified_date timestamp,

    url                varchar(255),
    file_name          varchar(255),
    extension          varchar(255)
);

create table comment
(
    comment_id   bigint AUTO_INCREMENT PRIMARY KEY,
    created_date timestamp,
    content      varchar(255) not null,
    portfolio_id bigint,
    user_id      BINARY(16)
);

create table company
(
    company_id         bigint AUTO_INCREMENT PRIMARY KEY,
    created_date       timestamp,
    address            varchar(255) not null,
    ceo                varchar(255) not null,
    form               varchar(255) not null,
    founding_date      varchar(255),
    homepage           varchar(255) not null,
    industry           varchar(255) not null,
    last_modified_date timestamp,
    member_count       varchar(255),
    name               varchar(255) not null,
    sales              varchar(255) not null,
    user_id            BINARY(16)
);

create table faq
(
    faq_id             bigint AUTO_INCREMENT PRIMARY KEY,
    created_date       timestamp,
    content            varchar(255) not null,
    last_modified_date timestamp,
    title              varchar(255) not null,
    user_id            BINARY(16)
);

create table follow
(
    follow_id    bigint AUTO_INCREMENT PRIMARY KEY,
    created_date timestamp,
    follower     BINARY(16),
    following    BINARY(16)
);

create table likes
(
    likes_id     bigint AUTO_INCREMENT PRIMARY KEY,
    created_date timestamp,
    portfolio_id bigint,
    user_id      BINARY(16)
);

create table notice
(
    notice_id          bigint AUTO_INCREMENT PRIMARY KEY,
    created_date       timestamp,
    content            varchar(255) not null,
    last_modified_date timestamp,
    title              varchar(255) not null,
    view_count         integer,
    user_id            BINARY(16)
);

create table portfolio
(
    portfolio_id       bigint AUTO_INCREMENT PRIMARY KEY,
    user_id            BINARY(16),
    title              varchar(255) not null,
    description        varchar(500) not null,
    category           varchar(255) not null,
    is_public          boolean,
    view_count         integer,
    extension          varchar(255),
    file_name          varchar(255),
    url                varchar(255),
    created_date       timestamp,
    last_modified_date timestamp
);

create table portfolio_content_files
(
    portfolio_id bigint not null,
    extension    varchar(255),
    file_name    varchar(255),
    url          varchar(255)
);

create table qna
(
    qna_id             bigint AUTO_INCREMENT PRIMARY KEY,
    dtype              varchar(31)  not null,
    created_date       timestamp    not null,
    content            varchar(255) not null,
    is_public          boolean,
    last_modified_date timestamp,
    title              varchar(255) not null,
    category           varchar(255),
    email              varchar(255),
    name               varchar(255),
    phone_number       varchar(255),
    status             varchar(255),

    question_id        bigint,
    user_id            BINARY(16)
);

create table recruitment
(
    recruitment_id     bigint AUTO_INCREMENT PRIMARY KEY,
    created_date       timestamp,
    title              varchar(255) not null,
    address            varchar(255) not null,
    career             varchar(255),
    category           varchar(255) not null,
    education          varchar(255),
    last_modified_date timestamp,
    logo               varchar(255),
    pay                varchar(255) not null,
    view_count         integer,
    work_type          varchar(255),
    company_id         bigint
);

create table user_company
(
    user_company_id bigint AUTO_INCREMENT PRIMARY KEY,
    company_id      bigint,
    user_id         BINARY(16)
);

create table user_portfolio
(
    user_portfolio_id bigint AUTO_INCREMENT PRIMARY KEY,
    portfolio_id      bigint,
    user_id           BINARY(16)
);

create table user_roles
(
    user_id BINARY(16) not null,
    roles   varchar(255)
);

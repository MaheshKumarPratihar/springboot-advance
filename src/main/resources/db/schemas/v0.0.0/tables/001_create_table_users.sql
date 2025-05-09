-- liquibase formatted sql
-- changeset task_5:create_users_table
CREATE TABLE ${schema.name}.users (
    id UUID NOT NULL,
    fullname VARCHAR NOT NULL,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    PRIMARY KEY(id)
);
-- rollback DROP TABLE IF EXISTS ${schema.name}."users" CASCADE
-- liquibase formatted sql
-- changeset task_5:create_role_table
CREATE TABLE ${schema.name}.role (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR NOT NULL,
    PRIMARY KEY(id)
);
-- rollback DROP TABLE IF EXISTS ${schema.name}."role" CASCADE
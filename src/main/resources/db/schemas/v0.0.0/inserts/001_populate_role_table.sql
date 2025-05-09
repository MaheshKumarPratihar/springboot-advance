-- liquibase formatted sql
-- changeset task_5:insert_in_roles_table
INSERT INTO ${schema.name}.role("name") VALUES('USER'), ('ADMIN'), ('MANAGER');
-- rollback DELETE FROM ${schema.name}.role WHERE id IN (0, 1, 2);
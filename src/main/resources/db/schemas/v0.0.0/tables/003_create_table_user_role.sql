-- liquibase formatted sql
-- changeset task_5:create_user_role_table
CREATE TABLE ${schema.name}.user_role (
    user_id UUID NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE
);

-- rollback DROP TABLE IF EXISTS ${schema.name}."user_role" CASCADE;
-- liquibase formatted sql

-- changeset akochkina:1

CREATE INDEX student_name_index ON student (name);

-- liquibase formatted sql

-- changeset akochkina:2

CREATE INDEX faculties_nc_idx ON faculties (color, name);
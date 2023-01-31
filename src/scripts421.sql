ALTER TABLE student
    ADD CONSTRAINT age_constraint CHECK (age > 16);

ALTER TABLE student
    ADD CONSTRAINT name_unique UNIQUE (name);

ALTER TABLE student
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE faculties
    ADD CONSTRAINT color_name_unique UNIQUE (color, name);

ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;
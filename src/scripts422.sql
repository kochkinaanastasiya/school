CREATE TABLE cars
(
    id INTEGER PRIMARY KEY,
    brand VARCHAR,
    model VARCHAR,
    cost INTEGER
);

CREATE TABLE users
(
    id INTEGER PRIMARY KEY,
    name VARCHAR,
    age INTEGER,
    license BOOLEAN,
    car_id INTEGER REFERENCES cars(id)
);
CREATE TABLE seats (
                       seat_id SERIAL PRIMARY KEY,
                       seat_number VARCHAR(10),
                       row VARCHAR(10),
                       type VARCHAR(20),
                       cinema_id INTEGER
);

CREATE TABLE seats (
                       seat_id SERIAL PRIMARY KEY,
                       seat_number SERIAL,
                       row VARCHAR(10),
                       type VARCHAR(20),
                       cinema_id SERIAL,
                       is_occupied BOOLEAN DEFAULT false

);
DROP TABLE IF EXISTS schedules;
CREATE TABLE schedules (
                           schedule_id SERIAL PRIMARY KEY,
                           movie_id INT NOT NULL,
                           cinema_id INT NOT NULL,
                           date DATE NOT NULL,
                           start_time TIME NOT NULL,
                           end_time TIME NOT NULL,
                           available_seats INT NOT NULL
);

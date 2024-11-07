
CREATE TABLE schedules (
                           schedule_id SERIAL PRIMARY KEY,
                           movie_id INT NOT NULL,
                           cinema_id INT NOT NULL,
                           date DATE NOT NULL,
                           start_time TIME NOT NULL,
                           end_time TIME NOT NULL,
                           available_seats INT NOT NULL

);

INSERT INTO schedules(movie_id, cinema_id, date, start_time, end_time, available_seats) VALUES (1,1,'12.03.2024','10:00:00','12:00:00',22);


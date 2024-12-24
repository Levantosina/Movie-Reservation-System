
CREATE TABLE ticket (
                        ticket_id SERIAL PRIMARY KEY,
                        user_id BIGINT,
                        movie_id BIGINT,
                        cinema_id BIGINT,
                        seat_id BIGINT,
                        schedule_id BIGINT,
                        price DECIMAL(10, 2),
                        date DATE
);
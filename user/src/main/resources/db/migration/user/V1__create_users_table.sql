


CREATE TABLE users
(
    user_id    SERIAL PRIMARY KEY,
    first_name varchar(25)  NOT NULL,
    last_name  varchar(25)  NOT NULL,
    email      varchar(100)  NOT NULL UNIQUE,
    password   varchar(100) NOT NULL,
    role_name VARCHAR(50)
);



 INSERT INTO users (first_name, last_name, email, password, role_name)
 VALUES ('Admin', 'Admin', 'admin@admin.com', '$2a$10$YbWNcZ52v1Pr9AC/5ydq2eLd7bFt52BDDvEdiO8eKu/Gxq0PfNZ.K','ROLE_ADMIN');

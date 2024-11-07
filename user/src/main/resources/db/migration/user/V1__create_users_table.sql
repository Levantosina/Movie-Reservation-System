DROP TABLE IF EXISTS seats;
DROP TABLE IF EXISTS users;
CREATE TABLE roles (
                       role_id SERIAL PRIMARY KEY,
                       role_name VARCHAR(25) NOT NULL ,
                       description VARCHAR(100) NOT NULL
);
CREATE TABLE users(
                      user_id SERIAL PRIMARY KEY ,
                      first_name varchar(25) NOT NULL ,
                      last_name varchar(25)NOT NULL,
                      email varchar (50)NOT NULL UNIQUE ,
                    password varchar(100) NOT NULL,
                      role_id SERIAL NOT NULL ,
                      CONSTRAINT fk_role FOREIGN KEY (role_id)
                          REFERENCES roles (role_id)
                          ON DELETE CASCADE
);


INSERT INTO ROLES(role_id,role_name,description)VALUES (1,'ROLE_ADMIN','admin');
INSERT INTO ROLES(role_id,role_name,description)VALUES (2,'ROLE_USER','user');

INSERT INTO users (first_name, last_name, email, password, role_id)
VALUES ('Admin', 'Admin', 'admin@admin.com', '$2a$10$YbWNcZ52v1Pr9AC/5ydq2eLd7bFt52BDDvEdiO8eKu/Gxq0PfNZ.K',
        (SELECT roles.role_id FROM roles WHERE role_name = 'ROLE_ADMIN'));
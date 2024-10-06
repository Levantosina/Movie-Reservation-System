CREATE TABLE roles (
                       role_id SERIAL PRIMARY KEY,
                       role_name VARCHAR(25) NOT NULL ,
                       description VARCHAR(100) NOT NULL
);
CREATE TABLE users(
                      user_id SERIAL PRIMARY KEY ,
                      first_name varchar(25) NOT NULL ,
                      last_name varchar(25)NOT NULL,
                      email varchar (50)NOT NULL,
                      role_id SERIAL NOT NULL ,
                      CONSTRAINT fk_role FOREIGN KEY (role_id)
                          REFERENCES roles (role_id)
                          ON DELETE CASCADE
);


INSERT INTO ROLES(role_id,role_name,description)VALUES (1,'admin','admin');
INSERT INTO ROLES(role_id,role_name,description)VALUES (2,'customer','customer');
CREATE DATABASE IF NOT EXISTS to_do_list_backend;
USE to_do_list_backend;
CREATE TABLE user(
                     email VARCHAR(20) PRIMARY KEY,
                     name VARCHAR(30) NOT NULL ,
                     password VARCHAR(10) NOT NULL
);
CREATE TABLE items(
                      id int AUTO_INCREMENT PRIMARY KEY,
                      description VARCHAR(100) NOT NULL ,
                      email VARCHAR(30) NOT NULL,
                      status ENUM('DONE','NOT-DONE'),
                      CONSTRAINT fk_key FOREIGN KEY (email) REFERENCES user(email)

);

CREATE DATABASE student_db;

USE student_db;

CREATE TABLE classmates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dob DATE NOT NULL
);

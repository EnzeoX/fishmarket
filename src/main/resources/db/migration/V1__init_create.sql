CREATE SCHEMA IF NOT EXISTS fishstore;

CREATE TABLE IF NOT EXISTS fish
(
    id int NOT NULL AUTO_INCREMENT,
    catch_date datetime(6) DEFAULT NULL,
    image_file_name varchar(255) DEFAULT NULL,
    name varchar(255) DEFAULT NULL,
    price double NOT NULL,
    PRIMARY KEY (id)
)
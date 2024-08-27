START TRANSACTION;

CREATE TABLE IF NOT EXISTS user_table
(
    id int NOT NULL AUTO_INCREMENT,
    password varchar(255) NOT NULL,
    username varchar(255) NOT NULL UNIQUE,
    role varchar(11) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS data_file
(
    data_id   INT          NOT NULL AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(255) NOT NULL,
    save_date DATETIME(6)  NOT NULL,
    fish_id   INT          NOT NULL,
    PRIMARY KEY (data_id),
    FOREIGN KEY (fish_id) REFERENCES fish (fish_id)
);

DELIMITER //

CREATE TRIGGER before_insert_fish
    BEFORE INSERT ON fish
    FOR EACH ROW
BEGIN
    INSERT INTO data_file (file_name, file_type, save_date, fish_id)
    VALUES (
               OLD.image_file_name,
               SUBSTRING_INDEX(OLD.image_file_name, '.', -1),
               OLD.catch_date,
               OLD.id
           );
END; //

DELIMITER ;

INSERT INTO data_file (file_name, file_type, save_date, fish_id)
SELECT image_file_name, SUBSTRING_INDEX(image_file_name, '.', -1), catch_date, id
FROM fish;

ALTER TABLE fish
    CHANGE COLUMN id fish_id INT NOT NULL AUTO_INCREMENT,
    DROP COLUMN image_file_name;

COMMIT;
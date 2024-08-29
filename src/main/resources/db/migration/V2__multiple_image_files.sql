# CREATE TABLE IF NOT EXISTS fish
# (
#     id int NOT NULL AUTO_INCREMENT,
#     catch_date datetime(6) DEFAULT NULL,
#     image_file_name varchar(255) DEFAULT NULL,
#     name varchar(255) DEFAULT NULL,
#     price double NOT NULL,
#     PRIMARY KEY (id)
# );
#
# START TRANSACTION;
#
# DELIMITER //
# DROP TRIGGER IF EXISTS before_insert_fish //
# CREATE TRIGGER before_insert_fish
#     AFTER INSERT
#     ON fish
#     FOR EACH ROW
# BEGIN
#     INSERT INTO data_file (file_name, file_type, save_date, fish_id)
#     VALUES (NEW.image_file_name,
#             SUBSTRING_INDEX(NEW.image_file_name, '.', -1),
#             NEW.catch_date,
#             NEW.id);
# END //
#
# DELIMITER ;
#
# CREATE TABLE IF NOT EXISTS data_file
# (
#     data_id   INT          NOT NULL AUTO_INCREMENT,
#     file_name VARCHAR(255) NOT NULL,
#     file_type VARCHAR(255) NOT NULL,
#     save_date DATETIME(6)  NOT NULL,
#     fish_id   INT          NOT NULL,
#     PRIMARY KEY (data_id),
#     FOREIGN KEY (fish_id) REFERENCES fish (id)
# );
#
# CREATE TABLE IF NOT EXISTS user_table
# (
#     id       INT          NOT NULL AUTO_INCREMENT,
#     username VARCHAR(255) NOT NULL,
#     password VARCHAR(255) NOT NULL,
#     role     VARCHAR(12)  NOT NULL,
#     PRIMARY KEY (id)
# );
#
# INSERT INTO data_file (file_name, file_type, save_date, fish_id)
# SELECT image_file_name,
#        SUBSTRING_INDEX(image_file_name, '.', -1),
#        catch_date,
#        id
# FROM fish;
#
# DROP TRIGGER before_insert_fish;
#
# DELIMITER //
#
# DROP PROCEDURE IF EXISTS DropForeignKey //
# CREATE PROCEDURE DropForeignKey()
# BEGIN
#     DECLARE fk_name VARCHAR(255);
#
#     SELECT CONSTRAINT_NAME
#     INTO fk_name
#     FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
#     WHERE TABLE_NAME = 'data_file'
#       AND REFERENCED_TABLE_NAME = 'fish';
#
#     SET @sql = CONCAT('ALTER TABLE data_file DROP FOREIGN KEY ', fk_name);
#
#     PREPARE stmt FROM @sql;
#     EXECUTE stmt;
#     DEALLOCATE PREPARE stmt;
# END //
#
# DELIMITER ;
#
# CALL DropForeignKey();
# DROP PROCEDURE  IF EXISTS DropForeignKey;
#
# ALTER TABLE fish
#     DROP COLUMN image_file_name,
#     CHANGE COLUMN id fish_id INT NOT NULL AUTO_INCREMENT;
#
# ALTER TABLE data_file
#     ADD FOREIGN KEY (fish_id) REFERENCES fish (fish_id);
#
# COMMIT;

CREATE TABLE IF NOT EXISTS fish
(
    id int NOT NULL AUTO_INCREMENT,
    catch_date datetime(6) DEFAULT NULL,
    image_file_name varchar(255) DEFAULT NULL,
    name varchar(255) DEFAULT NULL,
    price double NOT NULL,
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
    FOREIGN KEY (fish_id) REFERENCES fish(id)
);

CREATE TABLE IF NOT EXISTS user_table
(
    id       INT          NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(12)  NOT NULL,
    PRIMARY KEY (id)
);

DELIMITER //
CREATE TRIGGER before_insert_fish AFTER INSERT ON fish
    FOR EACH ROW
BEGIN
    IF NOT EXISTS (SELECT *
                   FROM data_file
                   WHERE file_name = SUBSTRING_INDEX(image_file_name, '.', 1) AND file_type = SUBSTRING_INDEX(image_file_name, '.', -1) AND save_date = catch_date AND data_file.fish_id = id)
    THEN
        INSERT INTO data_file (file_name, file_type, save_date, fish_id)
        VALUES (SUBSTRING_INDEX(image_file_name, '.', 1), SUBSTRING_INDEX(image_file_name, '.', -1), catch_date, id);
    END IF;
END //

DELIMITER ;

INSERT INTO data_file (file_name, file_type, save_date, fish_id)
SELECT image_file_name,
       SUBSTRING_INDEX(image_file_name, '.', -1),
       catch_date,
       id
FROM fish;

DROP TRIGGER before_insert_fish;

DELIMITER //
DROP PROCEDURE IF EXISTS DropForeignKey //
CREATE PROCEDURE DropForeignKey()
BEGIN
    DECLARE fk_name VARCHAR(255);

    SELECT CONSTRAINT_NAME
    INTO fk_name
    FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
    WHERE TABLE_NAME = 'data_file'
      AND REFERENCED_TABLE_NAME = 'fish';

    SET @sql = CONCAT('ALTER TABLE data_file DROP FOREIGN KEY ', fk_name);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END //
DELIMITER ;

CALL DropForeignKey();
DROP PROCEDURE IF EXISTS DropForeignKey;

ALTER TABLE fish
    DROP COLUMN image_file_name,
    CHANGE COLUMN id fish_id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE data_file
    ADD FOREIGN KEY (fish_id) REFERENCES fish (fish_id);
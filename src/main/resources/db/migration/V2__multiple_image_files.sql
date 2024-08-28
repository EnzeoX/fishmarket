START TRANSACTION;

DELIMITER //
DROP TRIGGER IF EXISTS before_insert_fish //
CREATE TRIGGER before_insert_fish
    AFTER INSERT
    ON fish
    FOR EACH ROW
BEGIN
    INSERT INTO data_file (file_name, file_type, save_date, fish_id)
    VALUES (image_file_name,
            SUBSTRING_INDEX(image_file_name, '.', -1),
            catch_date,
            id);
END //

DELIMITER ;

CREATE TABLE IF NOT EXISTS data_file
(
    data_id   INT          NOT NULL AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(255) NOT NULL,
    save_date DATETIME(6)  NOT NULL,
    fish_id   INT          NOT NULL,
    PRIMARY KEY (data_id),
    FOREIGN KEY (fish_id) REFERENCES fish (id)
);

CREATE TABLE IF NOT EXISTS user_table
(
    id       INT          NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(12)  NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO data_file (file_name, file_type, save_date, fish_id)
SELECT image_file_name,
       SUBSTRING_INDEX(image_file_name, '.', -1),
       catch_date,
       id
FROM fish;

DROP TRIGGER before_insert_fish;
-- SELECT TABLE_NAME,COLUMN_NAME,CONSTRAINT_NAME,
-- REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE REFERENCED_TABLE_NAME = 'fish';
-- ALTER TABLE data_file
--     DROP FOREIGN KEY data_file_ibfk_1;

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

ALTER TABLE fish
    DROP COLUMN image_file_name,
    CHANGE COLUMN id fish_id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE data_file
    ADD FOREIGN KEY (fish_id) REFERENCES fish (fish_id);

COMMIT;
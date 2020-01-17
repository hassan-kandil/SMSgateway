CREATE TABLE Message (
    id int NOT NULL AUTO_INCREMENT,
    Phone varchar(255) NOT NULL,
    Body text,
    SentFlag int DEFAULT 0,
    ts datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);



INSERT INTO Message (Phone,Body) VALUES ('32133232','hello2');

SELECT id, Phone, Body FROM Message WHERE `SentFlag` = 0 ORDER BY `ts` LIMIT 1;

UPDATE Message SET SentFlag = 1 WHERE id=1;
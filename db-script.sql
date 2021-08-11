DROP DATABASE IF EXISTS dep7;

CREATE DATABASE dep7;

USE dep7;

CREATE TABLE customer(
    id VARCHAR(4) PRIMARY KEY,
    nic VARCHAR(10) UNIQUE KEY,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(350)  NOT NULL
);

INSERT INTO customer VALUES ('C001','123456789v','Prasad','Pitabeddara');
INSERT INTO customer VALUES ('C002','223456789v','Rashmi','Darangala');

--ALTER TABLE customer DROP CONSTRAINT address;
--ALTER TABLE customer ADD CONSTRAINT uk_key UNIQUE KEY  (nic);

-- INSERT INTO customer VALUES('C003','741852741V','Viduranga','Matara');


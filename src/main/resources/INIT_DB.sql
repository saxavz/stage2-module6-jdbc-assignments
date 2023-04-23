--DROP TABLE IF EXISTS PUBLIC.MYUSERS;

CREATE TABLE IF NOT EXISTS PUBLIC.MYUSERS(
    --id integer primary key generated always as identity,
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(1024),
    lastname VARCHAR(1024),
    age    int
);

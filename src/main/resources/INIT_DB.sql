--DROP TABLE IF EXISTS PUBLIC.MYUSERS;

CREATE TABLE IF NOT EXISTS PUBLIC.MYUSERS(
    id integer primary key generated always as identity,
    firstname VARCHAR(1024),
    lastname VARCHAR(1024),
    age    int
);

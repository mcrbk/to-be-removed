DROP TABLE IF EXISTS TRANSFER;
DROP TABLE IF EXISTS ACCOUNT;

CREATE TABLE TRANSFER (
    ACCOUNT_FROM VARCHAR,
    ACCOUNT_TO VARCHAR,
    AMOUNT VARCHAR,
    STATUS VARCHAR
);

CREATE TABLE ACCOUNT (
    ACCOUNT_NUMBER VARCHAR,
    BALANCE VARCHAR
);
--liquibase formatted sql

--changeset applicant:1
CREATE TABLE if NOT EXISTS applicant (
    external_user_id VARCHAR(128) PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    dob DATE,
    level_name VARCHAR(128));

INSERT INTO applicant (external_user_id, first_name, last_name, dob, level_name )
VALUES (1,'test', 'test', '2000-01-01', 'basic-kyc-level');
--rollback DROP TABLE applicant;

--changeset webhook:1
CREATE TABLE if NOT EXISTS webhook (
    id SERIAL PRIMARY KEY,
    webhook_name VARCHAR(128),
    webhook_time TIMESTAMP,
    external_user_id VARCHAR(128));

INSERT INTO webhook (id, webhook_name, webhook_time, external_user_id )
VALUES (0,'test', '2024-06-09 19:10:25','1');
--rollback DROP TABLE webhook;
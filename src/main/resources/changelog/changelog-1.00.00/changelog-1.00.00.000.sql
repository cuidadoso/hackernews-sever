--liquibase formatted sql

--changeset apyreev:ALPERE.changelog-1.00.00.000-001  failOnError:false context:update
--comment ADD global sequence app_seq
CREATE SEQUENCE app_seq
INCREMENT BY 1
MINVALUE 1
START WITH 1000;

--changeset apyreev:ALPERE.changelog-1.00.00.000-002 failOnError:true context:update
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:1 select count(*) from pg_class where relkind = 'S' and relname = 'app_seq'
--comment CHECK global sequence app_seq
SELECT 1;

--changeset apyreev:ALPERE.changelog-1.00.00.000-003  failOnError:false context:update
--comment ADD users table
CREATE TABLE users
(
  id            BIGINT                      NOT NULL,
  name          CHARACTER VARYING(20)       NOT NULL,
  email         CHARACTER VARYING(20)       NOT NULL,
  password      CHARACTER VARYING(20)       NOT NULL,
  created_by    BIGINT                      NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE    NOT NULL,
  updated_by    BIGINT                      NOT NULL,
  updated_at    TIMESTAMP WITH TIME ZONE    NOT NULL,
  deleted_by    BIGINT,
  deleted_at    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT pkey_users PRIMARY KEY (id)
)
WITH (
OIDS = FALSE
);
ALTER TABLE users
  OWNER TO "alpere";

CREATE UNIQUE INDEX uc_users_email
  ON users (email)
  WHERE deleted_at IS NULL;

--changeset apyreev:ALPERE.changelog-1.00.00.000-004 failOnError:true context:update
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:1 select count(*) from pg_class where relkind = 'r' and relname = 'users'
--comment CHECK user table
SELECT 1;

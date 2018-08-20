--liquibase formatted sql

--changeset apyreev:ALPERE.changelog-1.00.00.001-001  failOnError:false context:update
--comment ADD links table
CREATE TABLE links
(
  id            BIGINT                      NOT NULL,
  url           CHARACTER VARYING(100)       NOT NULL,
  description   CHARACTER VARYING(1000),
  user_id       BIGINT                      NOT NULL,
  created_by    BIGINT                      NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE    NOT NULL,
  updated_by    BIGINT                      NOT NULL,
  updated_at    TIMESTAMP WITH TIME ZONE    NOT NULL,
  deleted_by    BIGINT,
  deleted_at    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT pkey_links PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES  users (id)
)
WITH (
OIDS = FALSE
);
ALTER TABLE links
  OWNER TO "alpere";

CREATE UNIQUE INDEX uc_links_url
  ON links (url)
  WHERE deleted_at IS NULL;

--changeset apyreev:ALPERE.changelog-1.00.00.001-002 failOnError:true context:update
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:1 select count(*) from pg_class where relkind = 'r' and relname = 'links'
--comment CHECK links table
SELECT 1;

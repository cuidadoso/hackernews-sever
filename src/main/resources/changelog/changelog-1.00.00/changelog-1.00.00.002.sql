--liquibase formatted sql

--changeset apyreev:ALPERE.changelog-1.00.00.002-001  failOnError:false context:update
--comment ADD vote table
CREATE TABLE vote
(
  id            BIGINT                      NOT NULL,
  user_id       BIGINT                      NOT NULL,
  link_id       BIGINT                      NOT NULL,
  created_by    BIGINT                      NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE    NOT NULL,
  updated_by    BIGINT                      NOT NULL,
  updated_at    TIMESTAMP WITH TIME ZONE    NOT NULL,
  deleted_by    BIGINT,
  deleted_at    TIMESTAMP WITH TIME ZONE,
  CONSTRAINT pkey_vote PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES  users (id),
  FOREIGN KEY (link_id) REFERENCES  links (id)
)
WITH (
OIDS = FALSE
);
ALTER TABLE vote
  OWNER TO "alpere";

CREATE UNIQUE INDEX uc_vote_user_link
  ON vote (user_id, link_id)
  WHERE deleted_at IS NULL;

--changeset apyreev:ALPERE.changelog-1.00.00.002-002 failOnError:true context:update
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:1 select count(*) from pg_class where relkind = 'r' and relname = 'vote'
--comment CHECK vote table
SELECT 1;

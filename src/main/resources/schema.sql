DROP TABLE IF EXISTS Post;

CREATE TABLE Post (
  id varchar NOT NULL,
  sourceIdentifier varchar(255) NOT NULL,
  published varchar(255) NOT NULL,
  lastModified varchar(255) NOT NULL,
  vulnStatus varchar(255) NOT NULL,
  PRIMARY KEY (id)
);
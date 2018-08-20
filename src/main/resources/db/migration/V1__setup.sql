CREATE TABLE ACCOUNTS (
  id          IDENTITY NOT NULL,
  balance     DECIMAL  NOT NULL,
  holder_name VARCHAR  NOT NULL,
  CONSTRAINT accounts_id PRIMARY KEY (id)
);

CREATE TABLE TRANSACTIONS (
  id          IDENTITY NOT NULL,
  source      INTEGER  NOT NULL,
  destination INTEGER  NOT NULL,
  amount      DECIMAL  NOT NULL,
  CONSTRAINT transactions_id PRIMARY KEY (id)
);
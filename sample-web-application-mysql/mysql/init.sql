CREATE TABLE IF NOT EXISTS Customer (
   id bigint PRIMARY KEY AUTO_INCREMENT,
   first_name text NOT NULL,
   last_name text NOT NULL
);

CREATE TABLE IF NOT EXISTS Account (
   id bigint PRIMARY KEY AUTO_INCREMENT,
   balance bigint,
   creation_date timestamp
);

CREATE TABLE IF NOT EXISTS Customer_Account_ref (
   id bigint PRIMARY KEY AUTO_INCREMENT,
   customer_id bigint,
   account_id bigint,
    FOREIGN KEY (customer_id)
         REFERENCES Customer (id),
    FOREIGN KEY (account_id)
         REFERENCES Account (id)
);

CREATE TABLE IF NOT EXISTS Action (
   id bigint PRIMARY KEY AUTO_INCREMENT,
   title text,
   amount bigint,
   type text,
   account_id bigint,
   status text,
   date timestamp,
   FOREIGN KEY (account_id)
       REFERENCES Account (id)
);

INSERT INTO Customer(id, first_name, last_name) VALUES (1, 'Emiliano', 'Graham');
INSERT INTO Customer(id, first_name, last_name) VALUES (2, 'Jeremiah', 'Washington');
INSERT INTO Customer(id, first_name, last_name) VALUES (3, 'Jennifer', 'Medina');
INSERT INTO Customer(id, first_name, last_name) VALUES (4, 'Vitoria', 'Kelly');
INSERT INTO Customer(id, first_name, last_name) VALUES (5, 'Mazda', 'Knight');
INSERT INTO Customer(id, first_name, last_name) VALUES (6, 'Brenda', 'Tucker');
INSERT INTO Customer(id, first_name, last_name) VALUES (7, 'Stevie','Alvarez');
INSERT INTO Customer(id, first_name, last_name) VALUES (8, 'Carl', 'Lee');
INSERT INTO Customer(id, first_name, last_name) VALUES (9, 'Leon', 'Hawkins');
INSERT INTO Customer(id, first_name, last_name) VALUES (10, 'Constance', 'Lynch');

INSERT INTO Account(id, balance, creation_date) VALUES (1, 100, '1995-11-21 04:05:06');
INSERT INTO Account(id, balance, creation_date) VALUES (2, 12500, '1999-01-08 16:09:39');
INSERT INTO Account(id, balance, creation_date) VALUES (3, 1, '1990-03-18 10:55:59');
INSERT INTO Account(id, balance, creation_date) VALUES (4, 679,'1999-06-02 13:13:13');
INSERT INTO Account(id, balance, creation_date) VALUES (5, 12, '2000-05-14 08:25:26');
INSERT INTO Account(id, balance, creation_date) VALUES (6, 1000000, '2005-12-24 18:44:46');
INSERT INTO Account(id, balance, creation_date) VALUES (7, 600100, '2003-09-16 15:09:27');

INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(1,1);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(2,2);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(3,3);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(4,4);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(5,5);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(6,6);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(7,7);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(8,7);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(9,1);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(10,1);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(1,3);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(2,6);
INSERT INTO Customer_Account_ref(customer_id, account_id) VALUES(6,2);

INSERT INTO Action (title, amount, type, account_id, status, date)
VALUES('cash withdrawals 2', 500, 'cash withdrawals', 1 ,'done', '2004-09-16 15:20:47');
INSERT INTO Action (title, amount, type, account_id, status, date)
VALUES('some title', 16, 'online payments', 2 ,'done', '2014-07-26 15:29:24');
INSERT INTO Action (title, amount, type, account_id, status, date)
VALUES('Grocery', 80, 'debit card charges', 3 ,'done', '2013-10-08 14:10:58');
INSERT INTO Action (title, amount, type, account_id, status, date)
VALUES('Trip', 750, 'debit card charges', 4 ,'done', '2020-11-09 11:47:17');
INSERT INTO Action (title, amount, type, account_id, status, date)
VALUES('cash withdrawals 1', 1000, 'cash withdrawals', 5 ,'failed', '2019-02-12 10:09:16');

-- docker-compose down --volumes
-- docker-compose up --build
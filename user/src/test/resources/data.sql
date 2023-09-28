CREATE TABLE IF NOT EXISTS USERTABLE (userid VARCHAR(50) NOT NULL PRIMARY KEY,password VARCHAR(50),firstName VARCHAR(50),lastName VARCHAR(50),token VARCHAR(100),id INTEGER);

insert into USERTABLE(userid, password, firstName, lastName, token, id) values ('maxime', 'pass1234', 'Maxime', 'Muster', 'N0FlE_wwjoCG2mfqLPYq0NP8TIco8vb-','2');
insert into USERTABLE(userid, password, firstName, lastName, token, id) values ('jane', 'pass1234', 'Jane', 'Doe', 'yAY-txa3783OSn1Gyjm9Nk69zPkZz55h', '1');
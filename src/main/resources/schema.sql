drop table  IF EXISTS users CASCADE;
drop table  IF EXISTS items CASCADE;
drop table  IF EXISTS bookings CASCADE;
drop table  IF EXISTS comments CASCADE;
--
--CREATE TABLE IF NOT EXISTS users ( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(40), email varchar(40),
--CONSTRAINT AK_email UNIQUE(email));
--
--CREATE TABLE IF NOT EXISTS items ( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(40), description varchar(1000), isFree boolean, user_id INTEGER,
--CONSTRAINT fk_items_to_users FOREIGN KEY(user_id) REFERENCES users(id), UNIQUE(id, user_id) );
--
--CREATE TABLE IF NOT EXISTS bookings ( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, startTime timestamp, endTime timestamp, isAccepted boolean, user_id INTEGER, item_id INTEGER, comment varchar(1000),
--CONSTRAINT fk_bookings_to_users FOREIGN KEY(user_id) REFERENCES users(id), CONSTRAINT fk_bookings_to_items FOREIGN KEY(item_id) REFERENCES items(id), UNIQUE(id, user_id) );
--
--CREATE TABLE IF NOT EXISTS comments ( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, user_id INTEGER, item_id INTEGER, comment varchar(1000),
--CONSTRAINT fk_comments_to_users FOREIGN KEY(user_id) REFERENCES users(id), CONSTRAINT fk_comments_to_items FOREIGN KEY(item_id) REFERENCES items(id) );
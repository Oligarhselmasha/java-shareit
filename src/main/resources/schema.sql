drop table  IF EXISTS users CASCADE;
drop table  IF EXISTS items CASCADE;
drop table  IF EXISTS bookings CASCADE;
drop table  IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users ( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(40), email varchar(40),
CONSTRAINT AK_email UNIQUE(email));

CREATE TABLE IF NOT EXISTS items ( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(40), description varchar(1000), is_Free boolean, user_id INTEGER,
CONSTRAINT fk_items_to_users FOREIGN KEY(user_id) REFERENCES users(id), UNIQUE(id, user_id) );

CREATE TABLE IF NOT EXISTS comments ( id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, user_id INTEGER, item_id INTEGER, text varchar(1000), created TIMESTAMP,
CONSTRAINT fk_comments_to_users FOREIGN KEY(user_id) REFERENCES users(id), CONSTRAINT fk_comments_to_items FOREIGN KEY(item_id) REFERENCES items(id) );

CREATE TABLE IF NOT EXISTS bookings ( booking_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, start_time TIMESTAMP, end_time TIMESTAMP, status varchar(1000), comment varchar(1000), item_id INTEGER, user_id INTEGER,
CONSTRAINT fk_bookings_to_items FOREIGN KEY(item_id) REFERENCES items(id), CONSTRAINT fk_bookings_to_users FOREIGN KEY(user_id) REFERENCES users(id));
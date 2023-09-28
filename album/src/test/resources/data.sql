CREATE TABLE IF NOT EXISTS ALBUM (id INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,name VARCHAR(50) NOT NULL,year INT,artist VARCHAR(50));
insert into ALBUM(name, year, artist) values ('Live and More', 1968, 'Richard Harris');
insert into ALBUM(name, year, artist) values ('Starland Vocal Band', 1976, 'Starland Vocal Band');
insert into ALBUM(name, year, artist) values ('Song of Joy', 1976, 'Muskrat Love');

CREATE TABLE IF NOT EXISTS SONG (id INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,title VARCHAR(50) NOT NULL,artist VARCHAR(50) NOT NULL,label VARCHAR(50),released VARCHAR(50),album VARCHAR(50));
insert into SONG(title, artist, label, released, album) values ('MacArthur Park', 'Richard Harris', 'Dunhill Records', '1968', 'Live and More');
insert into SONG(title, artist, label, released, album) values ('Afternoon Delight', 'Starland Vocal Band', 'Windsong', '1976', 'Starland Vocal Band');
insert into SONG(title, artist, label, released, album) values ('Muskrat Love', 'Captain and Tennille', 'A&M', '1976', 'Song of Joy');
insert into SONG(title, artist, label, released, album) values ('My Boy', 'Richard Harris', 'Dunhill Records', '1968', 'Live and More');
insert into SONG(title, artist, label, released, album) values ('Starland', 'Starland Vocal Band', 'Windsong', '1976', 'Starland Vocal Band');
insert into SONG(title, artist, label, released, album) values ('Song of Joy', 'Muskrat Love', 'A&M', '1976', 'Song of Joy');

CREATE TABLE IF NOT EXISTS ALBUM_SONG (album_id INTEGER,song_id INTEGER, FOREIGN KEY (album_id) REFERENCES ALBUM (id),  FOREIGN KEY (song_id) REFERENCES SONG (id));
insert into ALBUM_SONG(album_id, song_id) values ('1', '1');
insert into ALBUM_SONG(album_id, song_id) values ('1', '4');
insert into ALBUM_SONG(album_id, song_id) values ('2', '2');
insert into ALBUM_SONG(album_id, song_id) values ('2', '5');
insert into ALBUM_SONG(album_id, song_id) values ('3', '3');
insert into ALBUM_SONG(album_id, song_id) values ('3', '6');
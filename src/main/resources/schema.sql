/*DROP TABLE IF EXISTS greetings, students;

CREATE TABLE greetings (
    greetings_id serial PRIMARY KEY,
    greetings varchar (255) NOT NULL,
    name varchar (255) NOT NULL,
    language varchar (255) NOT NULL
);

INSERT INTO greetings (greetings, name, language) VALUES ('Hello', 'world', 'english');
INSERT INTO greetings (greetings, name, language) VALUES ('Salut', 'le monde', 'french');
INSERT INTO greetings (greetings, name, language) VALUES ('Hola', 'el mundo', 'spanish');
INSERT INTO greetings (greetings, name, language) VALUES ('Saluton', 'mondo', 'esperanto');
INSERT INTO greetings (greetings, name, language) VALUES ('Ciao', 'mondo', 'italian');
INSERT INTO greetings (greetings, name, language) VALUES ('Hallo', 'welt', 'german');
INSERT INTO greetings (greetings, name, language) VALUES ('Hallo', 'wereld', 'dutch');



CREATE TABLE students (
	student_id serial PRIMARY KEY,
	login varchar(255) NOT NULL UNIQUE,
	firstname varchar (255) NOT NULL,
	lastname varchar (255) NOT NULL,
	birth date NOT NULL,
	UNIQUE (login)
);

INSERT INTO students (login, firstname, lastname, birth) VALUES ('thibs', 'Thibault', 'Coilliaux', '1995-05-13 14:00:00');
INSERT INTO students (login, firstname, lastname, birth) VALUES ('soso', 'Sofian', 'Neor', '1902-01-08 04:05:06');
INSERT INTO students (login, firstname, lastname, birth) VALUES ('vinz', 'Vincent', 'Leclercq', '1903-02-08 04:05:06');
INSERT INTO students (login, firstname, lastname, birth) VALUES ('clem', 'Clément', 'Descamps', '1904-03-08 04:05:06');*/


 

 

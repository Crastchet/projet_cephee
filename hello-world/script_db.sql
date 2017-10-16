DROP TABLE greetings IF EXISTS;

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

 

 

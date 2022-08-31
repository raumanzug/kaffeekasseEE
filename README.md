# kaffeekasseEE - transformed [kaffeekasse commandline tool](https://github.com/raumanzug/kaffeekasse) into a web solution.

## Getting up and running.

We need:

- java runtime (java 18),
- maven (3.8.6),
- mysql/mariadb


Prepare mysql database:

	CREATE USER kaffeekasse;
	CREATE DATABASE kaffeekasse;
	USE DATABASE kaffeekasse;
	GRANT ALL ON kaffeekasse.* TO 'kaffeekasse'@'localhost';

Next, checkout project in an empty dir:

	git clone https://github.com/raumanzug/kaffeekasseEE.git

and start it there:

	mvn liberty:run

after starting up (wait til `ready for a Smarter Planet` or similar arises in log messages)
pick a browser and use URL

	http://localhost:9080/kaffeekasse



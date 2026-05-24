-- Verificare rapida dupa pornirea microserviciilor
USE usersdb;
SELECT 'users' AS tabel, COUNT(*) AS nr FROM users;

USE FilmsDB;
SELECT 'filme' AS tabel, COUNT(*) AS nr FROM filme;
SELECT 'regizori' AS tabel, COUNT(*) AS nr FROM regizori;
SELECT 'producatori' AS tabel, COUNT(*) AS nr FROM producatori;

USE ActorsDB;
SELECT 'actori' AS tabel, COUNT(*) AS nr FROM actori;
SELECT 'film_actori' AS tabel, COUNT(*) AS nr FROM film_actori;

USE StatisticsDB;
SELECT 'statistici' AS tabel, COUNT(*) AS nr FROM statistici_filme;

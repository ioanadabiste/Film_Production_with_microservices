-- ATENTIE: sterge toate datele din bazele proiectului (tabelele sunt recreate de Hibernate la repornire)
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS usersdb;
DROP DATABASE IF EXISTS FilmsDB;
DROP DATABASE IF EXISTS ActorsDB;
DROP DATABASE IF EXISTS StatisticsDB;

SET FOREIGN_KEY_CHECKS = 1;

-- Apoi ruleaza: sql/01-creeaza-baze.sql
-- Reporneste microserviciile (DataInitializer insereaza date demo)

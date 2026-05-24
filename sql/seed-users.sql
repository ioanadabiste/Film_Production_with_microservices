-- Utilizatori demo (daca DataInitializer nu i-a creat)
-- Parole: angajat123, manager123, admin123
USE usersdb;

SET SQL_SAFE_UPDATES = 0;

INSERT INTO users (name, surname, email, password, phone, user_type)
SELECT 'Ion', 'Popescu', 'angajat@firma.ro', 'angajat123', '0721000001', 0
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'angajat@firma.ro');

INSERT INTO users (name, surname, email, password, phone, user_type)
SELECT 'Maria', 'Ionescu', 'manager@firma.ro', 'manager123', '0721000002', 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'manager@firma.ro');

INSERT INTO users (name, surname, email, password, phone, user_type)
SELECT 'Admin', 'Sistem', 'admin@firma.ro', 'admin123', '0721000003', 2
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@firma.ro');

SET SQL_SAFE_UPDATES = 1;

SELECT id, name, surname, email, user_type FROM users;

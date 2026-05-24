-- Creeaza bazele de date pentru toate microserviciile
CREATE DATABASE IF NOT EXISTS usersdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS FilmsDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ActorsDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS StatisticsDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

SELECT 'Baze create: usersdb, FilmsDB, ActorsDB, StatisticsDB' AS status;

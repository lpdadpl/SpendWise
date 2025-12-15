-- V2__Insert_Default_Data.sql content moved to data.sql

-- =======================================================
-- 1. INSERCIÓN DE USUARIOS DE PRUEBA CON ID FORZADO (1 y 2)
-- Contraseña 'password123' hasheada (BCrypt)
-- =======================================================


-- 1. Usuario ADMIN (ID = 1)
INSERT INTO app_user (id, email, password, role, username, created_at)
VALUES (1, 'admin@spendwise.com', '$2a$10$y65bUqU6E7xM7Dk2H1Zc.uZt.p.r.4G2J/6k3E5N9W9rK8S7n5.yC', 'ADMIN', 'AdminUser', NOW())
ON CONFLICT (id) DO NOTHING;

-- 2. Usuario USER (ID = 2)
INSERT INTO app_user (id, email, password, role, username, created_at)
VALUES (2, 'alberto@spendwise.com', '$2a$10$y65bUqU6E7xM7Dk2H1Zc.uZt.p.r.4G2J/6k3E5N9W9rK8S7n5.yC', 'USER', 'AlbertoUser', NOW())
ON CONFLICT (id) DO NOTHING;

-- 🔑 PASO 2: Reiniciar la secuencia para que el siguiente ID que la aplicación genere sea 3.
-- Esto asume que la secuencia se llama 'app_user_id_seq', que es el nombre por defecto de BIGSERIAL.
SELECT setval('app_user_id_seq', (SELECT MAX(id) FROM app_user), TRUE);


-- =======================================================
-- 2. INSERCIÓN DE CATEGORÍAS (Para el usuario Alberto - ID 2)
-- =======================================================
INSERT INTO category (name, type, user_id)
SELECT 'Salario', 'INCOME', 2
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Salario' AND user_id = 2);

INSERT INTO category (name, type, user_id)
SELECT 'Alquiler', 'EXPENSE', 2
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Alquiler' AND user_id = 2);

INSERT INTO category (name, type, user_id)
SELECT 'Comida', 'EXPENSE', 2
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Comida' AND user_id = 2);


-- =======================================================
-- 3. INSERCIÓN DE TRANSACCIONES (Usuario Alberto - ID 2)
-- =======================================================
-- Nota: Verificamos duplicados por descripción y usuario para evitar inserciones múltiples
INSERT INTO app_transaction (description, amount, transaction_type, user_id, category_id)
SELECT 'Nómina de Diciembre', 3500.00, 'INCOME', 2, (SELECT id FROM category WHERE name = 'Salario' AND user_id = 2)
WHERE NOT EXISTS (SELECT 1 FROM app_transaction WHERE description = 'Nómina de Diciembre' AND user_id = 2);

INSERT INTO app_transaction (description, amount, transaction_type, user_id, category_id)
SELECT 'Compra supermercado', 85.50, 'EXPENSE', 2, (SELECT id FROM category WHERE name = 'Comida' AND user_id = 2)
WHERE NOT EXISTS (SELECT 1 FROM app_transaction WHERE description = 'Compra supermercado' AND user_id = 2);

INSERT INTO app_transaction (description, amount, transaction_type, user_id, category_id)
SELECT 'Cena con amigos', 45.00, 'EXPENSE', 2, (SELECT id FROM category WHERE name = 'Comida' AND user_id = 2)
WHERE NOT EXISTS (SELECT 1 FROM app_transaction WHERE description = 'Cena con amigos' AND user_id = 2);


-- =======================================================
-- 4. INSERCIÓN DE UNA PLANTILLA BÁSICA (Usuario Alberto - ID 2)
-- =======================================================
INSERT INTO forecast_template (name, description, is_public, owner_id, created_at, structure)
SELECT 
    'Presupuesto Mensual Básico', 
    'Plantilla para seguimiento de gastos e ingresos recurrentes.', 
    TRUE,
    2, 
    NOW(),
    '{"type": "table", "rows": [{"name": "Ingresos por Salario", "transaction_ref_id": null, "transaction_type": "INCOME", "amount": 3500.00}, {"name": "Gastos en Comida", "transaction_ref_id": null, "transaction_type": "EXPENSE", "amount": 500.00}]}'
WHERE NOT EXISTS (SELECT 1 FROM forecast_template WHERE name = 'Presupuesto Mensual Básico' AND owner_id = 2);

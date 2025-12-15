-- V2__Insert_Default_Data.sql

-- =======================================================
-- 1. INSERCIÓN DE USUARIOS DE PRUEBA CON ID FORZADO (1 y 2)
-- Contraseña 'password123' hasheada (BCrypt)
-- =======================================================

-- 🛑 PASO 1: Desactivar la generación automática de IDs para la inserción manual.
ALTER TABLE app_user ALTER COLUMN id DROP DEFAULT;

-- 1. Usuario ADMIN (ID = 1)
INSERT INTO app_user (id, email, password, role, username, created_at)
VALUES
(
    1,
    'admin@spendwise.com',
    '$2a$10$y65bUqU6E7xM7Dk2H1Zc.uZt.p.r.4G2J/6k3E5N9W9rK8S7n5.yC',
    'ADMIN',
    'AdminUser',
    NOW()
);

-- 2. Usuario USER (ID = 2)
INSERT INTO app_user (id, email, password, role, username, created_at)
VALUES
(
    2,
    'alberto@spendwise.com',
    '$2a$10$y65bUqU6E7xM7Dk2H1Zc.uZt.p.r.4G2J/6k3E5N9W9rK8S7n5.yC',
    'USER',
    'AlbertoUser',
    NOW()
);

-- 🔑 PASO 2: Reiniciar la secuencia para que el siguiente ID que la aplicación genere sea 3.
-- Esto asume que la secuencia se llama 'app_user_id_seq', que es el nombre por defecto de BIGSERIAL.
SELECT setval('app_user_id_seq', 2, TRUE);


-- =======================================================
-- 2. INSERCIÓN DE CATEGORÍAS (Para el usuario Alberto - ID 2)
-- =======================================================
INSERT INTO category (name, type, user_id)
VALUES
('Salario', 'INCOME', 2),
('Alquiler', 'EXPENSE', 2),
('Comida', 'EXPENSE', 2);


-- =======================================================
-- 3. INSERCIÓN DE TRANSACCIONES (Usuario Alberto - ID 2)
-- =======================================================
INSERT INTO transaction (description, amount, transaction_type, user_id, category_id)
VALUES
('Nómina de Diciembre', 3500.00, 'INCOME', 2, (SELECT id FROM category WHERE name = 'Salario' AND user_id = 2)),
('Compra supermercado', 85.50, 'EXPENSE', 2, (SELECT id FROM category WHERE name = 'Comida' AND user_id = 2)),
('Cena con amigos', 45.00, 'EXPENSE', 2, (SELECT id FROM category WHERE name = 'Comida' AND user_id = 2));


-- =======================================================
-- 4. INSERCIÓN DE UNA PLANTILLA BÁSICA (Usuario Alberto - ID 2)
-- =======================================================
INSERT INTO forecast_template (name, description, is_public, owner_id, created_at, structure)
VALUES
(
    'Presupuesto Mensual Básico', 
    'Plantilla para seguimiento de gastos e ingresos recurrentes.', 
    TRUE,
    2, -- owner_id = Alberto
    NOW(),
    '{"type": "table", "rows": [{"name": "Ingresos por Salario", "transaction_ref_id": null, "transaction_type": "INCOME", "amount": 3500.00}, {"name": "Gastos en Comida", "transaction_ref_id": null, "transaction_type": "EXPENSE", "amount": 500.00}]}'
);
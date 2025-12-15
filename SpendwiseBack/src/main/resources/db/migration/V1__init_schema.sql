-- V2__Create_Remaining_Tables.sql

-- =======================================================
-- 1. ACTUALIZACIÓN / CREACIÓN DE LA TABLA PRINCIPAL 'app_user'
-- NOTA: Si ya existe una tabla 'users' de tu migración V1, 
-- necesitas renombrar y alterar la tabla en V1 o en esta V2.
-- Asumo que la tabla inicial 'users' se renombrará a 'app_user' 
-- y se actualizará con los campos faltantes (username, created_at)
-- =======================================================
ALTER TABLE users RENAME TO app_user;

ALTER TABLE app_user ADD COLUMN username VARCHAR(255) UNIQUE;
ALTER TABLE app_user ADD COLUMN created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW();

-- Actualiza los usuarios existentes con un valor de username (opcional, pero necesario si el campo es NOT NULL)
-- Este paso puede ser complicado si ya tienes muchos usuarios. Asumo que es una base de datos nueva.
UPDATE app_user SET username = split_part(email, '@', 1) WHERE username IS NULL;

-- Asegurarse de que los campos clave sean NOT NULL después de la inserción
ALTER TABLE app_user ALTER COLUMN username SET NOT NULL;


-- =======================================================
-- 2. TABLA CATEGORY
-- =======================================================
CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, -- Podría ser un ENUM si usas PostgreSQL
    user_id BIGINT NOT NULL,
    
    CONSTRAINT fk_category_user 
        FOREIGN KEY (user_id) 
        REFERENCES app_user (id) 
        ON DELETE CASCADE
);


-- =======================================================
-- 3. TABLA FORECAST_TEMPLATE
-- =======================================================
CREATE TABLE forecast_template (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    
    -- El campo de la estructura JSON
    structure TEXT, 
    
    CONSTRAINT fk_template_owner 
        FOREIGN KEY (owner_id) 
        REFERENCES app_user (id) 
        ON DELETE CASCADE
);


-- =======================================================
-- 4. TABLA TEMPLATE_LIKED (Tabla de unión M:N)
-- =======================================================
CREATE TABLE template_liked (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL, -- El usuario que dio 'Me Gusta'
    id_template BIGINT NOT NULL, -- La plantilla a la que se dio 'Me Gusta'
    
    CONSTRAINT fk_liked_owner 
        FOREIGN KEY (owner_id) 
        REFERENCES app_user (id) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_liked_template 
        FOREIGN KEY (id_template) 
        REFERENCES forecast_template (id) 
        ON DELETE CASCADE,
        
    -- Restringe que un usuario solo pueda dar 'Me Gusta' una vez a una plantilla
    CONSTRAINT uq_liked_template_owner UNIQUE (owner_id, id_template)
);


-- =======================================================
-- 5. TABLA TRANSACTION
-- =======================================================
CREATE TABLE transaction (
    id BIGSERIAL PRIMARY KEY,
    description TEXT,
    
    -- Usamos NUMERIC para precisión monetaria
    amount NUMERIC(10, 2) NOT NULL, 
    
    -- El tipo de transacción (INCOME/EXPENSE)
    transaction_type VARCHAR(50) NOT NULL, 
    
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    
    CONSTRAINT fk_transaction_user 
        FOREIGN KEY (user_id) 
        REFERENCES app_user (id) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_transaction_category 
        FOREIGN KEY (category_id) 
        REFERENCES category (id) 
        ON DELETE RESTRICT -- No eliminar la categoría si tiene transacciones
);

-- =======================================================
-- 6. INSERCIÓN DE DATOS DE PRUEBA ADICIONALES
-- Este bloque debe ir en un archivo Flyway separado (ej: V3__Insert_Test_Data.sql) 
-- si quieres mantener la creación de esquema separada de la inserción de datos.
-- =======================================================

-- Ya insertaste tus usuarios, ahora puedes añadir categorías de prueba:
-- Necesitamos saber los IDs de los usuarios 'admin@spendwise.com' y 'alberto@spendwise.com'
-- Asumimos que la primera migración ya insertó estos usuarios.

-- Ejemplo de cómo insertar categorías, asumiendo que el ID de 'admin' es 1 y 'alberto' es 2.
-- Si usas BIGSERIAL, los IDs se generan automáticamente.

/*
INSERT INTO category (name, type, user_id) VALUES
('Salario', 'INCOME', (SELECT id FROM app_user WHERE email = 'admin@spendwise.com')),
('Alquiler', 'EXPENSE', (SELECT id FROM app_user WHERE email = 'admin@spendwise.com')),
('Comida', 'EXPENSE', (SELECT id FROM app_user WHERE email = 'alberto@spendwise.com'));
*/
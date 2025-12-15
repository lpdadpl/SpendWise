-- V1__Create_All_Tables.sql

-- =======================================================
-- 1. TABLA APP_USER (El alias de 'users' en las Entidades)
-- =======================================================
CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- =======================================================
-- 2. TABLA CATEGORY
-- =======================================================
CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
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
    -- El campo para el JSON de la estructura de la plantilla
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
    owner_id BIGINT NOT NULL,
    id_template BIGINT NOT NULL,

    CONSTRAINT fk_liked_owner
        FOREIGN KEY (owner_id)
        REFERENCES app_user (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_liked_template
        FOREIGN KEY (id_template)
        REFERENCES forecast_template (id)
        ON DELETE CASCADE,

    CONSTRAINT uq_liked_template_owner UNIQUE (owner_id, id_template)
);

-- =======================================================
-- 5. TABLA TRANSACTION
-- =======================================================
CREATE TABLE transaction (
    id BIGSERIAL PRIMARY KEY,
    description TEXT,
    amount NUMERIC(10, 2) NOT NULL,
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
        ON DELETE RESTRICT
);
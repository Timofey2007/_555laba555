SET search_path TO bk_502775_2026;

-- Удаляем старые таблицы (если есть)
DROP TABLE IF EXISTS reagents_stock_moves CASCADE;
DROP TABLE IF EXISTS reagents_batches CASCADE;
DROP TABLE IF EXISTS reagents_items CASCADE;
DROP TABLE IF EXISTS reagents_users CASCADE;

-- 1. Таблица пользователей
CREATE TABLE reagents_users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(256) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- 2. Таблица реактивов
CREATE TABLE reagents_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    formula VARCHAR(32),
    cas VARCHAR(32),
    hazard_class VARCHAR(32),
    owner_id INT NOT NULL REFERENCES reagents_users(id) ON DELETE CASCADE,
    owner_name VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Таблица партий
CREATE TABLE reagents_batches (
    id SERIAL PRIMARY KEY,
    reagent_id INT NOT NULL REFERENCES reagents_items(id) ON DELETE CASCADE,
    label VARCHAR(64) NOT NULL,
    quantity_current DECIMAL(10,2) NOT NULL CHECK (quantity_current >= 0),
    unit VARCHAR(2) NOT NULL CHECK (unit IN ('G', 'ML')),
    location VARCHAR(64) NOT NULL,
    expires_at TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'ARCHIVED')),
    owner_id INT NOT NULL REFERENCES reagents_users(id) ON DELETE CASCADE,
    owner_name VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Таблица движений
CREATE TABLE reagents_stock_moves (
    id SERIAL PRIMARY KEY,
    batch_id INT NOT NULL REFERENCES reagents_batches(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('IN', 'OUT', 'DISCARD')),
    quantity DECIMAL(10,2) NOT NULL CHECK (quantity > 0),
    unit VARCHAR(2) NOT NULL CHECK (unit IN ('G', 'ML')),
    reason VARCHAR(128),
    owner_id INT NOT NULL REFERENCES reagents_users(id) ON DELETE CASCADE,
    owner_name VARCHAR(50),
    moved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Индексы
CREATE INDEX idx_reagents_items_owner ON reagents_items(owner_id);
CREATE INDEX idx_reagents_batches_reagent ON reagents_batches(reagent_id);
CREATE INDEX idx_reagents_batches_owner ON reagents_batches(owner_id);
CREATE INDEX idx_reagents_moves_batch ON reagents_stock_moves(batch_id);
CREATE INDEX idx_reagents_moves_owner ON reagents_stock_moves(owner_id);

-- Проверка
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'bk_502775_2026'
  AND table_name LIKE 'reagents_%'
ORDER BY table_name;
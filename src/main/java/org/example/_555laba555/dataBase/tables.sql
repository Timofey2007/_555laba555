-- 1. Таблица пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       login VARCHAR(255) UNIQUE NOT NULL,
                       password_hash TEXT NOT NULL,
                       role VARCHAR(50) DEFAULT 'USER',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       last_login TIMESTAMP
);

-- 2. Таблица реактивов (Reagent)
CREATE TABLE reagents (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(128) NOT NULL, -- MAX_NAME_LENGTH = 128 [1, 2]
                          formula VARCHAR(32),        -- MAX_FORMULA_LENGTH = 32 [1, 2]
                          cas VARCHAR(32),            -- MAX_CAS_LENGTH = 32 [1, 2]
                          hazard_class VARCHAR(32),   -- MAX_HAZARD_CLASS_LENGTH = 32 [1, 2]
                          owner_id BIGINT NOT NULL,   -- Связь 1:N с пользователем [1]
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_reagent_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- 3. Таблица партий (ReagentBatch)
CREATE TABLE reagent_batches (
                                 id BIGSERIAL PRIMARY KEY,
                                 reagent_id BIGINT NOT NULL REFERENCES reagents(id) ON DELETE CASCADE,
                                 label VARCHAR(64) NOT NULL,     -- MAX_LABEL_LENGTH = 64 [3, 4]
                                 quantity_current DOUBLE PRECISION NOT NULL DEFAULT 0,
                                 unit VARCHAR(10) NOT NULL,      -- G или ML (BatchUnit) [4, 5]
                                 location VARCHAR(64) NOT NULL,  -- MAX_LOCATION_LENGTH = 64 [3, 4]
                                 expires_at TIMESTAMP,
                                 status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE или ARCHIVED [4, 6]
                                 owner_id BIGINT NOT NULL,       -- Связь 1:N с пользователем
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_batch_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- 4. Таблица движений (StockMove)
CREATE TABLE stock_moves (
                             id BIGSERIAL PRIMARY KEY,
                             batch_id BIGINT NOT NULL REFERENCES reagent_batches(id) ON DELETE CASCADE,
                             type VARCHAR(10) NOT NULL,    -- IN, OUT, DISCARD (StockMoveType) [7, 8]
                             quantity DOUBLE PRECISION NOT NULL,
                             unit VARCHAR(10) NOT NULL,
                             reason VARCHAR(128),          -- MAX_REASON_LENGTH = 128 [7, 9]
                             owner_id BIGINT NOT NULL,     -- Связь 1:N с пользователем
                             moved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_move_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);-- 1. Таблица пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       login VARCHAR(255) UNIQUE NOT NULL,
                       password_hash TEXT NOT NULL,
                       role VARCHAR(50) DEFAULT 'USER',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       last_login TIMESTAMP
);

-- 2. Таблица реактивов (Reagent)
CREATE TABLE reagents (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(128) NOT NULL, -- MAX_NAME_LENGTH = 128 [1, 2]
                          formula VARCHAR(32),        -- MAX_FORMULA_LENGTH = 32 [1, 2]
                          cas VARCHAR(32),            -- MAX_CAS_LENGTH = 32 [1, 2]
                          hazard_class VARCHAR(32),   -- MAX_HAZARD_CLASS_LENGTH = 32 [1, 2]
                          owner_id BIGINT NOT NULL,   -- Связь 1:N с пользователем [1]
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_reagent_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- 3. Таблица партий (ReagentBatch)
CREATE TABLE reagent_batches (
                                 id BIGSERIAL PRIMARY KEY,
                                 reagent_id BIGINT NOT NULL REFERENCES reagents(id) ON DELETE CASCADE,
                                 label VARCHAR(64) NOT NULL,     -- MAX_LABEL_LENGTH = 64 [3, 4]
                                 quantity_current DOUBLE PRECISION NOT NULL DEFAULT 0,
                                 unit VARCHAR(10) NOT NULL,      -- G или ML (BatchUnit) [4, 5]
                                 location VARCHAR(64) NOT NULL,  -- MAX_LOCATION_LENGTH = 64 [3, 4]
                                 expires_at TIMESTAMP,
                                 status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE или ARCHIVED [4, 6]
                                 owner_id BIGINT NOT NULL,       -- Связь 1:N с пользователем
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_batch_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- 4. Таблица движений (StockMove)
CREATE TABLE stock_moves (
                             id BIGSERIAL PRIMARY KEY,
                             batch_id BIGINT NOT NULL REFERENCES reagent_batches(id) ON DELETE CASCADE,
                             type VARCHAR(10) NOT NULL,    -- IN, OUT, DISCARD (StockMoveType) [7, 8]
                             quantity DOUBLE PRECISION NOT NULL,
                             unit VARCHAR(10) NOT NULL,
                             reason VARCHAR(128),          -- MAX_REASON_LENGTH = 128 [7, 9]
                             owner_id BIGINT NOT NULL,     -- Связь 1:N с пользователем
                             moved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_move_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);
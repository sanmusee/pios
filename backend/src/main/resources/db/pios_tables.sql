CREATE TABLE IF NOT EXISTS macro_indicator (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(100) UNIQUE,
    name VARCHAR(200),
    category_id BIGINT,
    stat_type VARCHAR(50),
    unit VARCHAR(50),
    frequency VARCHAR(20),
    FOREIGN KEY (category_id) REFERENCES macro_category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS macro_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    indicator_id BIGINT NOT NULL,
    period DATE NOT NULL,
    value DECIMAL(20,6),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_indicator_period (indicator_id, period),
    INDEX idx_period (period),
    FOREIGN KEY (indicator_id) REFERENCES macro_indicator(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

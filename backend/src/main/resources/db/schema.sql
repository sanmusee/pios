CREATE DATABASE IF NOT EXISTS pios_macro DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE pios_macro;

CREATE TABLE IF NOT EXISTS macro_indicator (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(100),
    sub_category VARCHAR(100),
    stat_type VARCHAR(50),
    unit VARCHAR(50),
    frequency VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
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

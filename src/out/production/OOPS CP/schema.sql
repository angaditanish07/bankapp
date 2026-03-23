-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS upi_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE upi_system;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)  NOT NULL,
    mobile     VARCHAR(15)   NOT NULL UNIQUE,
    email      VARCHAR(100)  NOT NULL UNIQUE,
    vpa        VARCHAR(100)  NOT NULL UNIQUE,
    upi_pin    VARCHAR(10)   NOT NULL,
    balance    DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    created_at DATETIME      DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_vpa(vpa),
    INDEX idx_email(email)
);

-- Bank accounts table (multiple per user)
CREATE TABLE IF NOT EXISTS bank_accounts (
    account_id     INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(30)   NOT NULL UNIQUE,
    bank_name      VARCHAR(100)  NOT NULL,
    balance        DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    user_id        INT           NOT NULL,
    is_primary     BOOLEAN       DEFAULT FALSE,
    linked_at      DATETIME      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id(user_id)
);

-- Transactions table
CREATE TABLE IF NOT EXISTS transactions (
    txn_id       INT AUTO_INCREMENT PRIMARY KEY,
    sender_vpa   VARCHAR(100)  NOT NULL,
    receiver_vpa VARCHAR(100)  NOT NULL,
    amount       DECIMAL(15,2) NOT NULL,
    status       ENUM('SUCCESS','FAILED','PENDING') DEFAULT 'PENDING',
    note         VARCHAR(255),
    txn_date     DATETIME      DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_sender(sender_vpa),
    INDEX idx_receiver(receiver_vpa)
);

-- Seed test users (PIN = 1234) — only insert if table is empty
INSERT IGNORE INTO users (name, mobile, email, vpa, upi_pin, balance) VALUES
  ('Alice Demo', '9000000001', 'alice@demo.com', 'alice@upi', '1234', 5000.00),
  ('Bob Demo',   '9000000002', 'bob@demo.com',   'bob@upi',   '1234', 3000.00);

-- Seed bank accounts
INSERT IGNORE INTO bank_accounts (account_number, bank_name, balance, user_id, is_primary) VALUES
  ('ACC100001', 'State Bank of India', 5000.00, 1, TRUE),
  ('ACC200001', 'HDFC Bank',           3000.00, 2, TRUE);

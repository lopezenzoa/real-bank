CREATE DATABASE IF NOT EXISTS real_bank;
USE real_bank;

CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  dni VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  creation_date DATETIME,
  load_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE credentials (
  credential_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  user_id INT,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  permission VARCHAR(255),
  load_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE accounts (
  account_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  user_id INT,
  account_type VARCHAR(255),
  balance DOUBLE,
  creation_date DATETIME,
  load_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

/* Audit Tables */
CREATE TABLE users_audit (
  audit_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  user_id INT,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  dni VARCHAR(255),
  email VARCHAR(255),
  creation_date DATETIME,
  load_date DATETIME,
  update_date DATETIME,
  operation_type VARCHAR(10), -- 'INSERT', 'UPDATE', 'DELETE'
  operation_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE credentials_audit (
  audit_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  credential_id INT,
  user_id INT,
  username VARCHAR(255),
  password VARCHAR(255),
  permission VARCHAR(255),
  load_date DATETIME,
  update_date DATETIME,
  operation_type VARCHAR(10),
  operation_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts_audit (
  audit_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  account_id INT,
  user_id INT,
  account_type VARCHAR(255),
  balance DOUBLE,
  creation_date DATETIME,
  load_date DATETIME,
  update_date DATETIME,
  operation_type VARCHAR(10),
  operation_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

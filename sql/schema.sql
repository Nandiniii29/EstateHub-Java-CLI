CREATE DATABASE IF NOT EXISTS estatehub_db;
USE estatehub_db;

CREATE TABLE IF NOT EXISTS properties (
    property_id INT NOT NULL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    purpose VARCHAR(20) NOT NULL,
    property_type VARCHAR(30) NOT NULL,
    city VARCHAR(100) NOT NULL,
    locality VARCHAR(100) NOT NULL,
    property_address VARCHAR(255) DEFAULT NULL,
    price DECIMAL(15,2) NOT NULL,
    bedrooms INT NOT NULL,
    bathrooms INT NOT NULL,
    area DECIMAL(10,2) NOT NULL,
    furnishing_status VARCHAR(30) NOT NULL,
    description TEXT DEFAULT NULL,
    listing_status VARCHAR(20) NOT NULL,
    owner_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS inquiries (
    inquiry_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    property_id INT NOT NULL,
    inquiry_type VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'SUBMITTED', 'CANCELLED') DEFAULT 'PENDING'
);
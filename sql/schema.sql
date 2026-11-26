
CREATE DATABASE IF NOT EXISTS estatehub_db;
USE estatehub_db;

-- 1. USERS
CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('CUSTOMER', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

-- 2. PROPERTIES
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

-- 3. INQUIRIES
CREATE TABLE IF NOT EXISTS inquiries (
    inquiry_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    property_id INT NOT NULL,
    inquiry_type VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'SUBMITTED', 'CANCELLED') DEFAULT 'PENDING',

    CONSTRAINT fk_inquiries_user
        FOREIGN KEY (user_id) REFERENCES users(user_id),

    CONSTRAINT fk_inquiries_property
        FOREIGN KEY (property_id) REFERENCES properties(property_id)
);

-- 4. TOUR REQUESTS
CREATE TABLE IF NOT EXISTS tour_requests (
    request_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    property_id INT NOT NULL,
    tour_date DATE NOT NULL,
    details TEXT,
    status VARCHAR(30) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tour_user
        FOREIGN KEY (user_id) REFERENCES users(user_id),

    CONSTRAINT fk_tour_property
        FOREIGN KEY (property_id) REFERENCES properties(property_id)
);

-- 5. PROPERTY IMAGES
CREATE TABLE IF NOT EXISTS property_images (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    property_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    caption VARCHAR(255),
    is_primary TINYINT(1) DEFAULT 0,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_images_property
        FOREIGN KEY (property_id) REFERENCES properties(property_id)
);

-- 6. FAVORITES
CREATE TABLE IF NOT EXISTS favorites (
    user_id VARCHAR(50) NOT NULL,
    property_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, property_id),

    CONSTRAINT fk_favorites_user
        FOREIGN KEY (user_id) REFERENCES users(user_id),

    CONSTRAINT fk_favorites_property
        FOREIGN KEY (property_id) REFERENCES properties(property_id)
);

-- 7. ADMIN ACTIVITY LOG
CREATE TABLE IF NOT EXISTS admin_activity_log (
    activity_id INT AUTO_INCREMENT PRIMARY KEY,
    admin_user_id VARCHAR(50) NOT NULL,
    property_id INT NULL,
    action VARCHAR(100) NOT NULL,
    description TEXT,
    activity_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_activity_admin
        FOREIGN KEY (admin_user_id) REFERENCES users(user_id),

    CONSTRAINT fk_activity_property
        FOREIGN KEY (property_id) REFERENCES properties(property_id)
);

-- 8. RECENTLY VIEWED
CREATE TABLE IF NOT EXISTS recently_viewed (
    user_id VARCHAR(50) NOT NULL,
    property_id INT NOT NULL,
    viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, property_id),

    CONSTRAINT fk_recent_user
        FOREIGN KEY (user_id) REFERENCES users(user_id),

    CONSTRAINT fk_recent_property
        FOREIGN KEY (property_id) REFERENCES properties(property_id)
);

-- 9. PROPERTY COMPARISONS
CREATE TABLE IF NOT EXISTS property_comparisons (
    user_id VARCHAR(50) NOT NULL,
    property_id INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, property_id),

    CONSTRAINT fk_comparison_user
        FOREIGN KEY (user_id) REFERENCES users(user_id),

    CONSTRAINT fk_comparison_property
        FOREIGN KEY (property_id) REFERENCES properties(property_id)
);
# SQL Setup

This directory contains the database setup script for EstateHub.

## Files
- schema.sql: Creates the estatehub_db database and the properties and inquiries tables if they do not already exist.

## Database Setup
1. Start your MySQL server.
2. Open the MySQL command-line client.
3. Execute the following command:

SOURCE D:/EstateHub-Java-CLI/sql/schema.sql;

Replace the path if your project is stored elsewhere.

## Database Tables

### properties
Stores property listings, including title, purpose, type, location, price, area, furnishing status, listing status, and owner ID.

### inquiries
Stores customer inquiries, including user ID, property ID, inquiry type, message, creation date, and status.

## Notes
- The script uses CREATE TABLE IF NOT EXISTS.
- Existing tables and records are not overwritten.
- The script does not insert sample records.
- User accounts and tour requests are currently managed in memory.
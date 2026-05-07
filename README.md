# Movie Theatre Management System

A JavaFX desktop application for managing movie theatre operations with role-based access, secure login, ticket booking, payment records, showtime management, and maintenance reporting.

## Security Features

- Role-based access control for Manager, Staff, and Maintenance users
- BCrypt password hashing
- Account lockout after failed login attempts
- Input validation for booking and management forms
- Prepared SQL statements to reduce SQL injection risk
- Action logging for important system activities
- Threat modeling and security testing documentation

## Demo Data Notice

This project includes demo database data for academic testing only. User passwords in the SQL file are stored as BCrypt hashes, not plain-text passwords. Do not reuse these demo accounts or database records in production.
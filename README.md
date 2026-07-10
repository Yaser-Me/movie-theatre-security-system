# Movie Theatre Management System

A JavaFX desktop application for managing movie theatre operations with role-based access, secure login, ticket booking, showtime management, payments, maintenance reporting, and audit records.

## Main capabilities

- Movie and showtime management
- Ticket booking and payment records
- Manager, staff, and maintenance views
- Maintenance issue reporting
- Sales, popularity, and peak-hour reports
- User action history

## Security design

- Role-based authorization for Manager, Staff, and Maintenance users
- BCrypt password verification
- Prepared SQL statements for database queries
- Account lockout handling
- Input validation
- Action logging
- Threat-model and security-testing documentation

## Technology

- Java and JavaFX
- MySQL or MariaDB
- JDBC
- jBCrypt

## Repository contents

| Path | Purpose |
|---|---|
| `Theater/src/` | JavaFX application source |
| `theater.sql` | Database schema and academic demo data |
| `MTMS_Threat_Modelling.pdf` | Threat-model documentation |
| `SRS.pdf` | Software requirements specification |
| `VCG_SAST_test_results.txt` | Saved static-analysis output |
| `testing.pdf` | Project testing documentation |
| `accounts.example.txt` | Non-production example accounts |

## Run locally

1. Install a JDK, JavaFX, and MySQL or MariaDB.
2. Create a local database by importing `theater.sql`.
3. Configure the JDBC connection in `Theater/src/DBUtils.java`.
4. Add the MySQL JDBC driver and jBCrypt library to the project.
5. Run `Theater/src/App.java` from a Java IDE with JavaFX configured.

The default code expects a database named `theater` on `localhost:3306`.

## Academic-use notice

The included records are demonstration data. The project is intended for coursework and local testing, not production deployment. Production use would require environment-based database secrets, stronger error handling, dependency management, automated tests, and a deployment-specific security review.

# EstateHub - Java CLI Property Management System

A console-based property management application developed in Java as an academic team project. It demonstrates object-oriented programming, collections, exception handling, multithreading, JDBC, and MySQL database operations.

## Project Features

- Add, view, search, update, and delete property listings.
- Search properties by city and locality.
- Filter and sort property records.
- Calculate home loan EMIs.
- Register and log in users, update profiles, and change passwords.
- Submit, view, and cancel property inquiries.
- Request and view property tours.
- Admin features for property approval, rejection, dashboards, and inquiry reports.
- Import property records from CSV.
- Export inquiry reports to CSV.
- Multithreading, synchronization, producer-consumer, and executor framework demonstrations.

## Technologies Used

- Java (JDK 17 or later)
- MySQL
- JDBC
- MySQL Connector/J
- Java Collections Framework
- Java Concurrency API
- CSV file handling

## Project Structure

```text
EstateHub-Java-CLI/
├── src/com/estatehub/
│   ├── concurrency/
│   ├── database/
│   ├── exceptions/
│   ├── main/
│   ├── model/
│   ├── repository/
│   ├── service/
│   └── util/
├── sql/
├── tests/
├── evidence/
│   ├── milestone-1/
│   ├── milestone-2/
│   ├── milestone-3/
│   ├── milestone-4/
│   └── milestone-5/
├── sample-properties.csv
├── lib/
│   └── mysql-connector-j-26.10.0.jar
├── .gitignore
└── README.md
```

## Prerequisites

1. Java JDK 17 or later.
2. MySQL Server.
3. MySQL Connector/J JAR.
4. A MySQL database named `estatehub_db`.

Place the MySQL Connector/J JAR in the `lib` directory. The JAR is a dependency and is not committed to this repository.

## Database Configuration

The application connects to:

```text
jdbc:mysql://localhost:3306/estatehub_db
```

Configure these environment variables before running the application:

- `ESTATEHUB_DB_USER` — your MySQL username.
- `ESTATEHUB_DB_PASSWORD` — your MySQL password.

The database must contain the `properties` and `inquiries` tables with the columns expected by the application repositories. See the SQL setup instructions in the `sql` directory.

## Compile and Run (Windows PowerShell)

Open PowerShell in the project root directory.

### 1. Configure database credentials

```powershell
$env:ESTATEHUB_DB_USER = "your_mysql_username"
$securePassword = Read-Host "Enter MySQL password" -AsSecureString
$env:ESTATEHUB_DB_PASSWORD = [System.Net.NetworkCredential]::new("", $securePassword).Password
```

Replace `your_mysql_username` with your own MySQL username. Do not commit database credentials.

### 2. Compile the project

```powershell
javac -cp "lib\mysql-connector-j-26.10.0.jar" -d out (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```

### 3. Run the application

```powershell
java -cp "out;lib\mysql-connector-j-26.10.0.jar" com.estatehub.main.Main
```

### 4. Test the database connection (optional)

```powershell
java -cp "out;lib\mysql-connector-j-26.10.0.jar" com.estatehub.main.DatabaseConnectionTest
```

## Data Storage Notes

- Property records are stored in MySQL.
- Customer inquiries are stored in MySQL.
- User accounts, profile changes, and tour requests currently use in-memory storage and are reset when the application process exits.
- CSV inquiry reports are generated in the `exports` directory.

## Academic Project

The project covers five broad learning areas:

1. OOP, strings, validation, and exception handling.
2. Collections, searching, filtering, and sorting.
3. Multithreading and synchronization.
4. Producer-consumer architecture and executor framework.
5. JDBC and relational database operations.

Screenshots and execution evidence for the academic milestones are maintained in the `evidence` directory.
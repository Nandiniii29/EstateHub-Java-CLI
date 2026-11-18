# EstateHub - Java CLI Property Management System

A modular, console-based Property Management Application in Java, adapted from the EstateHub real estate platform. Designed for academic pair programming and structured across 5 core Java milestones.

---

## 📌 Project Overview

**EstateHub Java CLI** allows property seekers, property owners, and platform administrators to manage, search, inspect, and evaluate real estate listings through an interactive command-line interface.

The application incorporates real-world property workflows including:
- Property listings, categorization, and specification tracking
- Search and multi-criteria filtering (location, purpose, price, type, amenities)
- User authentication and role-based access control (Seeker vs. Administrator)
- Inquiries and site tour request workflows
- Financial tools (reducing-balance EMI calculator and amortization schedule)
- Side-by-side property comparison

---

## 🎯 Academic Milestones

The project is developed progressively across five academic milestones:

| Milestone | Focus Area | Description |
| :--- | :--- | :--- |
| **Milestone 1** | **OOP, Strings & Exception Handling** | Domain modeling (`Property`, `User`, `Inquiry`), inheritance hierarchies, encapsulation, string parsing/formatting, input validation, and custom exception handling. |
| **Milestone 2** | **Collections & Record Management** | Managing in-memory datasets with Java Collections Framework (`List`, `Set`, `Map`), search & filter predicates, sorting (`Comparable`/`Comparator`), and file persistence. |
| **Milestone 3** | **Multithreading & Synchronization** | Concurrent request handling, critical sections, thread synchronization (`synchronized`, locks), and thread-safe data access. |
| **Milestone 4** | **Producer-Consumer & Executor Framework** | Producer-consumer architecture using thread-safe queues (e.g., inquiry ingestion and notification processing), worker pools via `ExecutorService`, and graceful shutdown. |
| **Milestone 5** | **JDBC & Relational Database** | Database persistence via JDBC, relational schema design (`properties`, `users`, `inquiries`), prepared statements, transactions, and DAO pattern. |

---

## 📁 Repository Structure

```
EstateHub-Java-CLI/
├── src/
│   └── com/
│       └── estatehub/
│           ├── main/          # Application entry point and CLI menu controller
│           ├── model/         # Domain entities (Property, User, Inquiry, enums)
│           ├── service/       # Business logic (Auth, Property, Inquiry, EMI calculation)
│           ├── repository/    # Data access interfaces and storage implementations
│           ├── exceptions/    # Custom checked and unchecked exceptions
│           ├── concurrency/   # Thread workers, synchronization, queues, and executors
│           ├── database/      # JDBC connection management, helpers, and DAOs
│           └── util/          # Utility helpers (formatting, validation, input reading)
├── sql/                       # SQL scripts for database DDL schemas and seed data
├── tests/                     # Test runners and milestone verification suites
├── evidence/                  # Execution logs, output captures, and milestone deliverables
│   ├── milestone-1/
│   ├── milestone-2/
│   ├── milestone-3/
│   ├── milestone-4/
│   └── milestone-5/
├── .gitignore                 # Standard Java, IDE, and OS ignore rules
└── README.md                  # Project documentation and roadmap
```

---

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 or higher (tested with modern OpenJDK/Oracle JDK).
- No external frameworks or package managers required.

### Compilation
From the project root:
```bash
javac -d bin -sourcepath src src/com/estatehub/main/Main.java
```

### Execution
```bash
java -cp bin com.estatehub.main.Main
```

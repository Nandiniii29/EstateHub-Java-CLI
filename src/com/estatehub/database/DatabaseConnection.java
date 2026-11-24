
package com.estatehub.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/estatehub";

    private static final String USER =
            System.getenv("ESTATEHUB_DB_USER");

    private static final String PASSWORD =
            System.getenv("ESTATEHUB_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        if (USER == null || PASSWORD == null) {
            throw new SQLException(
                    "Database username or password environment variable is missing."
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

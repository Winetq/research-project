package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final static String URL = "jdbc:postgresql://localhost/postgres";
    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private static Connection connection = null;

    public static Connection connect() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to PostgreSQL successfully.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return connection;
    }
}

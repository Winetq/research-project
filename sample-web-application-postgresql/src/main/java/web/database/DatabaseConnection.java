package web.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final static String URL = "jdbc:postgresql://localhost:5432/db";
    private final static String USER = "user";
    private final static String PASSWORD = "password";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to PostgreSQL successfully: " + connection);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }
}

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/rythma-db";
    private static final String USER = "anna";
    private static final String PASSWORD = "password";
    private static Connection connection = null;

    public static Connection connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to the database successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to the database.");
            e.printStackTrace();
        }
        return connection;
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.out.println("Could not disconnect from the database.");
            e.printStackTrace();
        }
    }

    public static void insertUser(String username, String password, String firstName, String lastName) {
        try {
            connection.createStatement().executeUpdate(
                    "INSERT INTO User (username, password, firstName, lastName) VALUES ('" + username + "', '" + password + "', '" + firstName + "', '" + lastName + "')");
            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Could not insert user.");
            e.printStackTrace();
        }
    }
}

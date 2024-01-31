package util;

import java.sql.*;

public class DBCon {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/agcv";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection connect() {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            System.out.println("connexion reussi");
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null; // Handle the exception appropriately in your application
        }
    }

    public static void disconnect(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately in your application
        }
    }
//    public static boolean validateCredentials(String username, String password) {
//        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
//            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
//            try (PreparedStatement statement = connection.prepareStatement(sql)) {
//                statement.setString(1, username);
//                statement.setString(2, password);
//                try (ResultSet resultSet = statement.executeQuery()) {
//                    return resultSet.next(); // If a record is found, credentials are valid
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
public static boolean validateCredentials(String username, String password) {
    try (Connection connection = connect()) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            System.out.println("Executing SQL: " + statement.toString()); // Log the SQL statement
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // If a record is found, credentials are valid
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public static void executeUpdateQuery(String query, Object... params) {
        try (Connection connection = connect()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Set parameters for the query
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }

                // Execute the update query
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Add this method to your DBCon class
// Add this method to your DBCon class
    public static String getEmailByUsername(String username) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT email FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("email");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no email is found or an error occurs
    }



}

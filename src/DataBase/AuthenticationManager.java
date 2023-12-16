package DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationManager{
    private static User loggedInUser;

    public static boolean authenticateUser(String username, String password) {
        User user = getUserByUsernameAndPassword(username, password);
        if (user != null) {
            loggedInUser = user;
            return true;
        } else {
            return false;
        }
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void logout() {
        loggedInUser = null;
    }

    public static boolean registerUser(String username, String password, String userType, String name, String surname) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = createRegisterUserPreparedStatement(connection, username, password, userType, name, surname)) {

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    private static User getUserByUsernameAndPassword(String username, String password) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = createLoginPreparedStatement(connection, username, password);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int id = resultSet.getInt("UserID");
                String userType = resultSet.getString("UserType");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                return new User(id, username, password, userType,name, surname);
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return null;
    }

    private static PreparedStatement createLoginPreparedStatement(Connection connection, String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE Username = ? AND Password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        return preparedStatement;
    }

    private static PreparedStatement createRegisterUserPreparedStatement(Connection connection, String username, String password, String userType, String name, String surname) throws SQLException {
        String query = "INSERT INTO users (Username, Password, UserType, Name, Surname) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, userType);
        preparedStatement.setString(4, name);
        preparedStatement.setString(5, surname);
        return preparedStatement;
    }

    private static void handleSQLException(SQLException e) {
        System.err.println("SQL Hatası: " + e.getMessage());
        e.printStackTrace();
        // İsterseniz burada daha fazla işlem yapabilirsiniz (loglama, kullanıcıya bilgi verme, vb.).
    }
}

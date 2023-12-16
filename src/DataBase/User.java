// User sınıfı
package DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class User extends BaseEntity implements CrudOperations<User> {

    private String username;
    private String password;
    private String userType;
    private String name;
    private String surname;

    public User(int id, String username, String password, String userType, String name, String surname) {
        super(id);
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.name = name ;
        this.surname = surname;
    }
    public User(int id,String username, String password, String userType){
        super(id);
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {this.userType = userType;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getSurname() {return surname;}

    public void setSurname(String surname) {this.surname = surname;}


    @Override
    public boolean add(User user) {
        String query = "INSERT INTO users (Username, Password, UserType, Name, Surname) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getUserType());
            preparedStatement.setString(4, user.getName());
            preparedStatement.setString(5, user.getSurname());

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        String query = "UPDATE users SET Username = ?, Password = ?, UserType = ?, Name = ?, Surname = ? WHERE UserID = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getUserType());
            preparedStatement.setInt(4, user.getId());

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM users WHERE UserID = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("UserID");
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");
                String userType = resultSet.getString("UserType");
                String name = resultSet.getString("Name");
                String surname = resultSet.getString("Surname");

                User retrievedUser = new User(id, username, password, userType,name,surname);
                users.add(retrievedUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private Connection getConnection() {
        return DataBaseConnection.getConnection();
    }

    public boolean login(String username, String password) {
        if (AuthenticationManager.authenticateUser(username, password)) {

            return true;
        } else {

            return false;
        }
    }

    public void logout() {
        AuthenticationManager.logout();
        System.out.println("Çıkış yapıldı.");
    }
}

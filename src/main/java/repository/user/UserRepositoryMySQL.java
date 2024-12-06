package repository.user;
import model.Book;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static database.Constants.Tables.USER;
import static java.util.Collections.singletonList;
import static java.util.Collections.synchronizedSet;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        String fetchUserSql = "SELECT * FROM `" + USER + "`";

        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(fetchUserSql);

            while (resultSet.next()) {
                User user = new UserBuilder()
                        .setUsername(resultSet.getString("username"))
                        .setPassword(resultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                        .build();

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // SQL Injection Attacks should not work after fixing functions
    // Be careful that the last character in sql injection payload is an empty space
    // alexandru.ghiurutan95@gmail.com' and 1=1; --
    // ' or username LIKE '%admin%'; --

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {


            String fetchUserSql =
                    "Select * from `" + USER + "` where `username` = ? and `password` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet userResultSet = preparedStatement.executeQuery();
            if (userResultSet.next()) {
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
                findByUsernameAndPasswordNotification.setResult(user);

            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something wrong with the Database!");
        }
        return findByUsernameAndPasswordNotification;

    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean delete(User user) {
        String newSql = "DELETE FROM user WHERE username=?;";

        try{
            PreparedStatement preparedStatement= connection.prepareStatement(newSql);
            preparedStatement.setString(1, user.getUsername());

            int deleted=preparedStatement.executeUpdate();
            return (deleted != 1) ? false:true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1, email);
            ResultSet userResultSet = preparedStatement.executeQuery();
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Long> findIdByUsername(String username) {
        String sql = " SELECT id FROM user WHERE username = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }

}
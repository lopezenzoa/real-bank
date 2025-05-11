package org.real.repository;

import org.real.database.MySQLConnection;
import org.real.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements IRepository<User> {
    private Connection connection;

    public UserDAO() {
        this.connection = MySQLConnection.getInstance().getConnection();
        createTable();
    }

    public void createTable() {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
                    "  user_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                    "  first_name VARCHAR(255),\n" +
                    "  last_name VARCHAR(255),\n" +
                    "  dni VARCHAR(255) UNIQUE NOT NULL,\n" +
                    "  email VARCHAR(255) UNIQUE NOT NULL,\n" +
                    "  creation_date DATETIME,\n" +
                    "  load_date DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
                    "  update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP\n" +
                    ");";

            stmt.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = " + id + ";";

        try (Statement stmt = connection.createStatement()) {
            // gets all rows of the table 'users'
            ResultSet rs = stmt.executeQuery(sql);

            // returns an optional if there's a match in the db
            if (rs.next())
                return Optional.of(createUser(rs));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users;";
        List<User> users = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            // gets all rows of the table 'users'
            ResultSet rs = stmt.executeQuery(sql);

            // adds all matches to the list
            while (rs.next())
                users.add(createUser(rs));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public Optional<User> save(User user) {
        // the 'creation_date' attribute is always the exact moment when a new user is added
        String sql = "INSERT INTO users(first_name, last_name, dni, email, creation_date) VALUES (?, ?, ?, ?, NOW());";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // builds the statement
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getDni());
            pstmt.setString(4, user.getEmail());

            // executes the statement after preparing it
            pstmt.executeUpdate();

            // gets the last id inserted in the table 'users'
            ResultSet rs = pstmt.executeQuery("SELECT LAST_INSERT_ID() AS user_id;");
            if (rs.next()) {
                user.setId(rs.getInt("user_id"));
                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // by default, if there's a problem, in addition to the exception execution, the method returns an empty user
        return Optional.empty();
    }

    @Override
    public boolean update(User newUser) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, dni = ?, email = ? WHERE user_id = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // builds the statement
            pstmt.setString(1, newUser.getFirstName());
            pstmt.setString(2, newUser.getLastName());
            pstmt.setString(3, newUser.getDni());
            pstmt.setString(4, newUser.getEmail());

            pstmt.setInt(5, newUser.getId()); // for WHERE statement in the query

            // executes the statement after preparing it
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM users WHERE user_id = " + id + ";";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User createUser(ResultSet rs) {
        User user = new User();

        try {
            user.setId(rs.getInt("user_id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setDni(rs.getString("dni"));
            user.setEmail(rs.getString("email"));
            user.setCreationDate(rs.getDate("creation_date").toLocalDate());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
}

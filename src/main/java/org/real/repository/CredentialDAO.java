package org.real.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.real.database.MySQLConnection;
import org.real.model.Credential;
import org.real.model.Permission;

public class CredentialDAO implements IRepository<Credential> {
    private Connection connection;

    public CredentialDAO() {
        this.connection = MySQLConnection.getInstance().getConnection();
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS credentials (\n" +
                "  credential_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  user_id INT,\n" +
                "  username VARCHAR(255) UNIQUE NOT NULL,\n" +
                "  password VARCHAR(255) NOT NULL,\n" +
                "  permission VARCHAR(255),\n" +
                "  load_date DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
                "  update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE\n" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Credential> findById(int id) {
        String sql = "SELECT * FROM credentials WHERE credential_id = " + id + ";";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next())
                return Optional.of(createCredential(rs));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Credential> findAll() {
        String sql = "SELECT * FROM credentials;";
        List<Credential> credentials = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
                credentials.add(createCredential(rs));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return List.of();
    }

    @Override
    public Optional<Credential> save(Credential credential) {
        String sql = "INSERT INTO credentials(user_id, username, password, permission) VALUES (?, ?, ?, ?);";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, credential.getUserId());
            pstmt.setString(2, credential.getUsername());
            pstmt.setString(3, credential.getPassword());
            pstmt.setString(4, credential.getPermission().toString());

            // everything is setted up, then it can be saved into the db
            pstmt.executeUpdate();

            // gets the last insert id from the table 'credentials'
            ResultSet rs = pstmt.executeQuery("SELECT LAST_INSERT_ID() AS credential_id;");

            if (rs.next()) {
                credential.setId(rs.getInt("credential_id"));
                return Optional.of(credential);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Credential newCredential) {
        // the record at the 'credentials' table is modified by giving his user_id attribute
        String sql = "UPDATE credentials SET username = ?, password = ?, permission = ? WHERE user_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newCredential.getUsername());
            pstmt.setString(2, newCredential.getPassword());
            pstmt.setString(3, newCredential.getPermission().toString());

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM credentials WHERE credential_id = " + id + ";";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Credential createCredential(ResultSet rs) {
        Credential credential = new Credential();

        try {
            credential.setId(rs.getInt("credential_id"));
            credential.setUserId(rs.getInt("user_id"));
            credential.setUsername(rs.getString("username"));
            credential.setUsername(rs.getString("password"));
            credential.setPermission(Permission.valueOf(rs.getString("permission")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return credential;
    }
}

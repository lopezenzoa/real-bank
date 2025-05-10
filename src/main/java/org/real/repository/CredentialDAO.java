package org.real.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String sql = "CREATE TABLE credentials (\n" +
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
    public int save(Credential type) {
        

        return 0;
    }

    @Override
    public void update(Credential newType) {

    }

    @Override
    public void deleteById(int id) {

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

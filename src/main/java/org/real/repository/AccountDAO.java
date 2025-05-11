package org.real.repository;

import org.real.database.MySQLConnection;
import org.real.model.Account;
import org.real.model.AccountType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAO implements IRepository<Account> {
    private Connection connection;

    public AccountDAO() {
        this.connection = MySQLConnection.getInstance().getConnection();
        createTable();
    }

    public void createTable() {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE accounts (\n" +
                    "  account_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                    "  user_id INT,\n" +
                    "  account_type VARCHAR(255),\n" +
                    "  balance DOUBLE,\n" +
                    "  creation_date DATETIME,\n" +
                    "  load_date DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
                    "  update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                    "  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE\n" +
                    ");";

            stmt.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Account> findById(int id) {
        String sql = "SELECT * FROM accounts WHERE account_id = " + id + ";";

        try (Statement stmt = connection.createStatement()) {
            // gets all rows of the table 'accounts'
            ResultSet rs = stmt.executeQuery(sql);

            // returns an optional if there's a match in the db
            if (rs.next())
                return Optional.of(createAccount(rs));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT * FROM accounts;";
        List<Account> accounts = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            // gets all rows of the table 'users'
            ResultSet rs = stmt.executeQuery(sql);

            // adds all matches to the list
            while (rs.next())
                accounts.add(createAccount(rs));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return accounts;
    }

    @Override
    public Optional<Account> save(Account account) {
        // the 'creation_date' attribute is always the exact moment when a new account is added
        // the 'balance' attribute is always 0.0 for a new account
        String sql = "INSERT INTO accounts(user_id, account_type, balance, creation_date) VALUES (?, ?, 0.0, NOW());";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // builds the statement
            pstmt.setInt(1, account.getUserId());
            pstmt.setString(2, account.getAccountType().toString());

            // executes the statement after preparing it
            pstmt.executeUpdate();

            // gets the last id inserted in the table 'accounts'
            ResultSet rs = pstmt.executeQuery("SELECT LAST_INSERT_ID() AS account_id;");
            if (rs.next()) {
                account.setId(rs.getInt("account_id"));
                return Optional.of(account);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // by default, if there's a problem, in addition to the exception execution, the method returns an empty account
        return Optional.empty();
    }

    @Override
    public boolean update(Account newAccount) {
        // the only attribute can be modified in an account is its balance
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // builds the statement
            pstmt.setDouble(1, newAccount.getBalance());

            pstmt.setInt(2, newAccount.getId()); // for WHERE statement in the query

            // executes the statement after preparing it
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM accounts WHERE account_id = " + id + ";";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Account createAccount(ResultSet rs) {
        Account account = new Account();

        try {
            account.setId(rs.getInt("account_id"));
            account.setUserId(rs.getInt("user_id"));
            account.setAccountType(AccountType.valueOf(rs.getString("account_type")));
            account.setBalance(rs.getDouble("balance"));
            account.setCreationDate(rs.getDate("creation_date").toLocalDate());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return account;
    }
}

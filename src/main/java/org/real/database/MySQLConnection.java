package org.real.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private String DB_NAME = "real_bank";
    private String URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private String USERNAME = "root";
    private String PASS = "root";

    private Connection connection;
    private static MySQLConnection instance;

    private MySQLConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USERNAME, PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MySQLConnection getInstance() {
        if (instance == null)
            return new MySQLConnection();
        else
            return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

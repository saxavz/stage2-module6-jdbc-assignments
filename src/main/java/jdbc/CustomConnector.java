package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CustomConnector {
    private final String dbUser;
    private final String dbPassword;
    private final String dbDriver;
    private final String dbUrl;

    public CustomConnector(String driver, String url, String password, String name) {
        this.dbUser = name;
        this.dbPassword = password;
        this.dbDriver = driver;
        this.dbUrl =url;
    }

    public Connection getConnection(String url) throws SQLException {
        return getConnection(url, this.dbUser, this.dbPassword);
    }

    public Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

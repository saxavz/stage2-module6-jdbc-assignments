package jdbc;

import javax.sql.DataSource;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;

    private final CustomConnector dbConnector;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.name = name;
        this.dbConnector = new CustomConnector(driver, url, password, name);
        initDb();
    }

    private void initDb(){

        Path initDbFilePath = Path.of(
                                    this.getClass().getResource("/INIT_DB.sql").getPath());
        String initSqlScript = "RAISE EXCEPTION \'\'Database has not been initialized\'\';";
        try {
            initSqlScript = Files.readString(initDbFilePath);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try(Connection conn = this.dbConnector.getConnection(this.url)){
            conn.prepareStatement(initSqlScript).execute();
        } catch (SQLException e){
            throw new RuntimeException("Something went wrong during dbInit phase", e);
        }
    }

    public static CustomDataSource getInstance() {
        if(Objects.isNull(instance)){
            synchronized (CustomDataSource.class){
                Properties props = PropertyLoader.loadProperties();
                instance = new CustomDataSource(
                                                props.getProperty("postgres.driver"),
                                                props.getProperty("postgres.url"),
                                                props.getProperty("postgres.password"),
                                                props.getProperty("postgres.name")
                );
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dbConnector.getConnection(this.url);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return dbConnector.getConnection(this.url, this.name, this.password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("not implemented");
    }
}

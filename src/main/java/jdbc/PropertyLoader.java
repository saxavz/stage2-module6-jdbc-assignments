package jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {
    public static Properties loadProperties() {
        Properties props = new Properties();

        try(InputStream inputStream = PropertyLoader.class
                .getClassLoader()
                .getResourceAsStream("app.properties")) {
            props.load(inputStream);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return props;
    }
}

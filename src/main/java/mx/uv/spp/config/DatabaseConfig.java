package mx.uv.spp.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = DatabaseConfig.class.getClassLoader()
            .getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("No se encontro el archivo db.properties");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la configuración de la BD", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            properties.getProperty("db.url"),
            properties.getProperty("db.user"),
            properties.getProperty("db.pass")
        );
    }
}
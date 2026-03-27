package mx.uv.spp.data.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());
    private static DatabaseConfig instance;

    private Connection connection;

    private DatabaseConfig() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        if (url == null || user == null || pass == null) {
            LOGGER.severe("Environment variables missing");
            return;
        }

        try {
            this.connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error", e);
        }
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
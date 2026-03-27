package mx.uv.spp.data.config;

import mx.uv.spp.data.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static DatabaseConfig instance;
    private Connection connection;

    private DatabaseConfig() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        if (url == null || user == null || pass == null) {
            throw new DatabaseException("Faltan variables de entorno DB_URL, DB_USER o DB_PASS", null);
        }

        try {
            this.connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new DatabaseException("Error critico al conectar con la base de datos", e);
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
package mx.uv.spp.data.config;

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
            System.out.println("Faltan variables de entorno");
            return;
        }

        try {
            this.connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Error de conexion: " + e.getMessage());
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
package mx.uv.spp.data.config;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

class DatabaseConfigTest {

    @Test
    void testSingleton() {
        DatabaseConfig instance1 = DatabaseConfig.getInstance();
        DatabaseConfig instance2 = DatabaseConfig.getInstance();

        assertNotNull(instance1, "La instancia es nulla");
        assertSame(instance1, instance2, "Debe retornar la misma instancia");
    }

    @Test
    void testGetConnection() {
        DatabaseConfig config = DatabaseConfig.getInstance();
        Connection connection = config.getConnection();

        assertNotNull(connection, "La conexion a la base de datos no debe ser nula");
    }
}
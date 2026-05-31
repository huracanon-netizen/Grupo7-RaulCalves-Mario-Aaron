package agencia_viajes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase utilitaria centralizada para:
 *  - Obtener conexiones JDBC a MySQL
 *  - Proveer el pool de hilos (ExecutorService) compartido por toda la app
 *
 * Cambiar URL, USUARIO o CONTRASENA si la configuración local es distinta.
 */
public class ConexionDB {

    // ── Datos de conexión ──────────────────────────────────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/agencia_viajes"
                                         + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "1234";   // <-- cambia aquí si tienes contraseña

    // ── Pool de hilos ──────────────────────────────────────────────────────
    // 4 hilos dedicados a operaciones de BD para no bloquear el EDT de Swing
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    // Constructor privado: no se instancia, todo es estático
    private ConexionDB() {}

    /**
     * Abre y devuelve una conexión nueva a la BD.
     * Cada llamada debe cerrarse con try-with-resources o con con.close().
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    /**
     * Devuelve el ExecutorService compartido de la aplicación.
     * Usar siempre este pool para lanzar operaciones de BD.
     */
    public static ExecutorService getExecutor() {
        return executor;
    }

    /**
     * Cierra el pool de hilos ordenadamente al salir de la aplicación.
     * Llamar desde el método main o al cerrar la ventana principal.
     */
    public static void cerrar() {
        executor.shutdown();
    }
}

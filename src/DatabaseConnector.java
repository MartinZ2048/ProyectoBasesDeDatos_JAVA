import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DatabaseConnector {

    private static final String URL_CONEXION = "jdbc:oracle:thin:@//localhost:1521/orcl";
    private static final String USUARIO = "maestronodo";
    private static final String CONTRASENA = "maestronodo";

    /**
     * --- CORRECCIÓN ---
     * Se cambió el modificador de acceso de 'private' a 'public' para que
     * otras clases (como las ventanas de gestión) puedan obtener una conexión
     * a la base de datos y usar PreparedStatement para mayor seguridad.
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(URL_CONEXION, USUARIO, CONTRASENA);
    }
    
    public TableModel query(String sql) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            return buildTableModel(rs);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
            e.printStackTrace(); 
            return new DefaultTableModel();
        }
    }

    public boolean executeUpdate(String sql) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            return true; 
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al ejecutar la actualización SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Registra una acción en la tabla de auditoría.
     * @param tablaAfectada El nombre de la tabla modificada (ej. "EMPLEADO").
     * @param operacion El tipo de operación ('INSERT', 'UPDATE', 'DELETE').
     * @param idRegistro El ID del registro que fue modificado.
     * @param datosNuevos Una descripción de los nuevos datos (opcional).
     */
    public void registrarAuditoria(String tablaAfectada, String operacion, String idRegistro, String datosNuevos) {
        // Se obtiene el usuario de la base de datos actual.
        String usuarioActual = "biblioteca_master"; 
        
        // --- IMPORTANTE: Esta implementación sigue siendo vulnerable a inyección SQL ---
        // Se recomienda refactorizar para usar PreparedStatement también aquí.
        String sql = String.format(
            "INSERT INTO AUDITORIA (tabla_afectada, operacion, id_registro, usuario, datos_nuevos) VALUES ('%s', '%s', '%s', '%s', '%s')",
            tablaAfectada, operacion, idRegistro, usuarioActual, datosNuevos
        );

        executeUpdate(sql);
    }

    public static TableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}

/*
 * =============================================================================
 * --- DatabaseConnector.java (VERSIÓN FINAL Y CORREGIDA) ---
 * Utiliza los datos de conexión verificados en la pestaña "Services"
 * para conectar la aplicación a la base de datos Oracle.
 * =============================================================================
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DatabaseConnector {

    // --- DATOS DE CONEXIÓN EXACTOS Y VERIFICADOS ---
    private static final String URL_CONEXION = "jdbc:oracle:thin:@//localhost:1521/orcl";
    private static final String USUARIO = "biblioteca_master";
    private static final String CONTRASENA = "biblioteca_master";

    /**
     * Obtiene una conexión a la base de datos.
     */
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        // Paso 1: Cargar el driver de Oracle explícitamente.
        // Esto resuelve el error "ClassNotFoundException".
        Class.forName("oracle.jdbc.driver.OracleDriver");
        
        // Paso 2: Utiliza los datos definidos arriba para establecer la conexión.
        return DriverManager.getConnection(URL_CONEXION, USUARIO, CONTRASENA);
    }
    
    /**
     * Ejecuta una consulta y devuelve los datos para una tabla.
     */
    public TableModel query(String sql) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            // Si todo es exitoso, crea y devuelve el modelo de la tabla.
            return buildTableModel(rs);

        } catch (SQLException | ClassNotFoundException e) {
            // Si ocurre cualquier error, se imprimirá en la consola de NetBeans.
            System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
            e.printStackTrace(); 
            return new DefaultTableModel(); // Devuelve una tabla vacía para que la GUI no se rompa.
        }
    }

    /**
     * Construye un DefaultTableModel a partir de un ResultSet.
     */
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

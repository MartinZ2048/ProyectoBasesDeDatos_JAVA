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

/**
 * Clase mejorada para gestionar la conexión y las operaciones con la base de datos Oracle.
 * Incluye métodos para consultas (SELECT) y actualizaciones (INSERT, UPDATE, DELETE).
 */
public class DatabaseConnector {

    // --- DATOS DE CONEXIÓN ---
    // Asegúrate de que estos datos coincidan con tu configuración de Oracle.
    private static final String URL_CONEXION = "jdbc:oracle:thin:@//localhost:1521/orcl";
    private static final String USUARIO = "biblioteca_master";
    private static final String CONTRASENA = "biblioteca_master";

    /**
     * Obtiene una conexión a la base de datos.
     */
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(URL_CONEXION, USUARIO, CONTRASENA);
    }
    
    /**
     * Ejecuta una consulta SELECT y devuelve los datos para una JTable.
     * @param sql La consulta SELECT a ejecutar.
     * @return Un TableModel con los resultados.
     */
    public TableModel query(String sql) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            return buildTableModel(rs);

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
            e.printStackTrace(); 
            return new DefaultTableModel(); // Devuelve una tabla vacía en caso de error.
        }
    }

    /**
     * --- MÉTODO NUEVO ---
     * Ejecuta una sentencia de actualización (INSERT, UPDATE, DELETE).
     * @param sql La sentencia SQL a ejecutar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean executeUpdate(String sql) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // executeUpdate devuelve el número de filas afectadas.
            // Si es mayor que 0, la operación tuvo éxito.
            stmt.executeUpdate(sql);
            return true; 

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al ejecutar la actualización SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Construye un DefaultTableModel a partir de un ResultSet.
     * (Sin cambios)
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

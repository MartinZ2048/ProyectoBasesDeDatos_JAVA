import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

public class VentanaVentas extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaVentas;

    // --- Componentes del Formulario ---
    private JTextField txtIdVenta, txtFechaVenta, txtIdCliente, txtIdEmpleado, txtTotal, txtIdSucursal;

    public VentanaVentas() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosVentas();
    }

    private void initComponents() {
        setTitle("Gestión de Ventas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel lblTituloVentana = new JLabel("\"VENTAS\" - Quicentro", JLabel.CENTER);
        lblTituloVentana.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblTituloVentana.setBounds(10, 10, 1160, 30);
        getContentPane().add(lblTituloVentana);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 750, 500);
        getContentPane().add(scrollPane);

        tablaVentas = new JTable();
        tablaVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaVentas.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaVentas);

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(245, 245, 245));
        panelDatos.setBounds(780, 50, 380, 500);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos", JLabel.CENTER);
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblDatosTitulo.setBounds(10, 10, 360, 25);
        panelDatos.add(lblDatosTitulo);

        txtIdVenta = crearCampo(panelDatos, "ID Venta:", 60);
        txtIdVenta.setEditable(false);
        txtFechaVenta = crearCampo(panelDatos, "Fecha Venta:", 110);
        txtFechaVenta.setEditable(false);
        txtIdCliente = crearCampo(panelDatos, "ID Cliente:", 160);
        txtIdEmpleado = crearCampo(panelDatos, "ID Empleado:", 210);
        txtTotal = crearCampo(panelDatos, "Total:", 260);
        txtIdSucursal = crearCampo(panelDatos, "ID Sucursal:", 310);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(null);
        panelBotones.setBounds(20, 560, 1140, 80);
        panelBotones.setBackground(new Color(245, 245, 245));
        getContentPane().add(panelBotones);
        
        JButton btnAgregar = crearBoton("Agregar", 200, 10, new Color(144, 238, 144), Color.BLACK);
        JButton btnModificar = crearBoton("Modificar", 400, 10, new Color(255, 255, 0), Color.BLACK);
        JButton btnEliminar = crearBoton("Eliminar", 600, 10, new Color(255, 99, 71), Color.WHITE);
        JButton btnRegresar = crearBoton("Regresar", 800, 10, new Color(255, 165, 0), Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRegresar);

        btnAgregar.addActionListener(e -> agregarVenta());
        btnModificar.addActionListener(e -> modificarVenta());
        btnEliminar.addActionListener(e -> eliminarVenta());
        btnRegresar.addActionListener(e -> this.dispose());

        tablaVentas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaVentas.getSelectedRow();
                if (filaSeleccionada != -1) {
                    txtIdVenta.setText(tablaVentas.getValueAt(filaSeleccionada, 0).toString());
                    txtFechaVenta.setText(tablaVentas.getValueAt(filaSeleccionada, 1) != null ? tablaVentas.getValueAt(filaSeleccionada, 1).toString() : "");
                    txtIdCliente.setText(tablaVentas.getValueAt(filaSeleccionada, 2) != null ? tablaVentas.getValueAt(filaSeleccionada, 2).toString() : "");
                    txtIdEmpleado.setText(tablaVentas.getValueAt(filaSeleccionada, 3) != null ? tablaVentas.getValueAt(filaSeleccionada, 3).toString() : "");
                    txtTotal.setText(tablaVentas.getValueAt(filaSeleccionada, 4) != null ? tablaVentas.getValueAt(filaSeleccionada, 4).toString() : "");
                    txtIdSucursal.setText(tablaVentas.getValueAt(filaSeleccionada, 5) != null ? tablaVentas.getValueAt(filaSeleccionada, 5).toString() : "");
                }
            }
        });
    }

    private void cargarDatosVentas() {
        String sql = "SELECT idVenta, TO_CHAR(fechaVenta, 'YYYY-MM-DD HH24:MI:SS') as fechaVenta, idCliente, idEmpleado, total, idSucursal FROM VENTA ORDER BY idVenta";
        tablaVentas.setModel(dbConnector.query(sql));
    }

    private void agregarVenta() {
        if (txtIdCliente.getText().trim().isEmpty() || txtIdEmpleado.getText().trim().isEmpty() || txtTotal.getText().trim().isEmpty() || txtIdSucursal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos (excepto ID y Fecha) son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO VENTA (idVenta, fechaVenta, idCliente, idEmpleado, total, idSucursal) VALUES ((SELECT NVL(MAX(idVenta), 0) + 1 FROM VENTA), SYSDATE, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(txtIdCliente.getText()));
            pstmt.setInt(2, Integer.parseInt(txtIdEmpleado.getText()));
            pstmt.setDouble(3, Double.parseDouble(txtTotal.getText()));
            pstmt.setInt(4, Integer.parseInt(txtIdSucursal.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Venta agregada exitosamente.");
                cargarDatosVentas();
                limpiarCampos();
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar la venta: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarVenta() {
        if (txtIdVenta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una venta de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE VENTA SET idCliente = ?, idEmpleado = ?, total = ?, idSucursal = ? WHERE idVenta = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtIdCliente.getText()));
            pstmt.setInt(2, Integer.parseInt(txtIdEmpleado.getText()));
            pstmt.setDouble(3, Double.parseDouble(txtTotal.getText()));
            pstmt.setInt(4, Integer.parseInt(txtIdSucursal.getText()));
            pstmt.setInt(5, Integer.parseInt(txtIdVenta.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Venta modificada exitosamente.");
                cargarDatosVentas();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la venta para modificar.", "Error de Modificación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar la venta: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarVenta() {
        if (txtIdVenta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una venta de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar esta venta?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM VENTA WHERE idVenta = ?";
            try (Connection conn = dbConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, Integer.parseInt(txtIdVenta.getText()));

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Venta eliminada exitosamente.");
                    cargarDatosVentas();
                    limpiarCampos();
                }
            } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar la venta: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limpiarCampos() {
        txtIdVenta.setText("");
        txtFechaVenta.setText("");
        txtIdCliente.setText("");
        txtIdEmpleado.setText("");
        txtTotal.setText("");
        txtIdSucursal.setText("");
        tablaVentas.clearSelection();
    }

    private JTextField crearCampo(JPanel panel, String etiqueta, int y) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(10, y, 120, 25);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(140, y, 220, 25);
        panel.add(txt);
        return txt;
    }

    private JButton crearBoton(String texto, int x, int y, Color colorFondo, Color colorTexto) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, 150, 40);
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        return boton;
    }
}

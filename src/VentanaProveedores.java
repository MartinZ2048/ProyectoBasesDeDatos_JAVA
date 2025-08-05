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

public class VentanaProveedores extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaProveedores;
    private JTextField txtIdProveedor, txtNombre, txtContacto, txtTelefono, txtEmail, txtDireccion;

    public VentanaProveedores() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosProveedores();
    }

    private void initComponents() {
        setTitle("Gestión de Proveedores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel lblTituloVentana = new JLabel("\"PROVEEDORES\" - Quicentro", JLabel.CENTER);
        lblTituloVentana.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblTituloVentana.setBounds(10, 10, 1160, 30);
        getContentPane().add(lblTituloVentana);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 750, 500);
        getContentPane().add(scrollPane);

        tablaProveedores = new JTable();
        tablaProveedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaProveedores.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaProveedores);

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(245, 245, 245));
        panelDatos.setBounds(780, 50, 380, 500);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos", JLabel.CENTER);
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblDatosTitulo.setBounds(10, 10, 360, 25);
        panelDatos.add(lblDatosTitulo);

        txtIdProveedor = crearCampo(panelDatos, "ID Proveedor:", 60);
        txtIdProveedor.setEditable(false);
        txtNombre = crearCampo(panelDatos, "Nombre:", 110);
        txtContacto = crearCampo(panelDatos, "Contacto:", 160);
        txtTelefono = crearCampo(panelDatos, "Teléfono:", 210);
        txtEmail = crearCampo(panelDatos, "Email:", 260);
        txtDireccion = crearCampo(panelDatos, "Dirección:", 310);

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

        btnAgregar.addActionListener(e -> agregarProveedor());
        btnModificar.addActionListener(e -> modificarProveedor());
        btnEliminar.addActionListener(e -> eliminarProveedor());
        btnRegresar.addActionListener(e -> this.dispose());

        tablaProveedores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaProveedores.getSelectedRow();
                if (filaSeleccionada != -1) {
                    txtIdProveedor.setText(tablaProveedores.getValueAt(filaSeleccionada, 0).toString());
                    txtNombre.setText(tablaProveedores.getValueAt(filaSeleccionada, 1).toString());
                    txtContacto.setText(tablaProveedores.getValueAt(filaSeleccionada, 2) != null ? tablaProveedores.getValueAt(filaSeleccionada, 2).toString() : "");
                    txtTelefono.setText(tablaProveedores.getValueAt(filaSeleccionada, 3) != null ? tablaProveedores.getValueAt(filaSeleccionada, 3).toString() : "");
                    txtEmail.setText(tablaProveedores.getValueAt(filaSeleccionada, 4) != null ? tablaProveedores.getValueAt(filaSeleccionada, 4).toString() : "");
                    txtDireccion.setText(tablaProveedores.getValueAt(filaSeleccionada, 5) != null ? tablaProveedores.getValueAt(filaSeleccionada, 5).toString() : "");
                }
            }
        });
    }

    private void cargarDatosProveedores() {
        String sql = "SELECT idProveedor, nombre, contacto, telefono, email, direccion FROM PROVEEDOR ORDER BY idProveedor";
        tablaProveedores.setModel(dbConnector.query(sql));
    }

    private void agregarProveedor() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del proveedor es obligatorio.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO PROVEEDOR (idProveedor, nombre, contacto, telefono, email, direccion) VALUES ((SELECT NVL(MAX(idProveedor), 0) + 1 FROM PROVEEDOR), ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtContacto.getText());
            pstmt.setString(3, txtTelefono.getText());
            pstmt.setString(4, txtEmail.getText());
            pstmt.setString(5, txtDireccion.getText());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Proveedor agregado exitosamente.");
                cargarDatosProveedores();
                limpiarCampos();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar el proveedor: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarProveedor() {
        if (txtIdProveedor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un proveedor de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE PROVEEDOR SET nombre = ?, contacto = ?, telefono = ?, email = ?, direccion = ? WHERE idProveedor = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtContacto.getText());
            pstmt.setString(3, txtTelefono.getText());
            pstmt.setString(4, txtEmail.getText());
            pstmt.setString(5, txtDireccion.getText());
            pstmt.setInt(6, Integer.parseInt(txtIdProveedor.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Proveedor modificado exitosamente.");
                cargarDatosProveedores();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el proveedor para modificar.", "Error de Modificación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar el proveedor: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProveedor() {
        if (txtIdProveedor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un proveedor de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este proveedor?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM PROVEEDOR WHERE idProveedor = ?";
            try (Connection conn = dbConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, Integer.parseInt(txtIdProveedor.getText()));

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Proveedor eliminado exitosamente.");
                    cargarDatosProveedores();
                    limpiarCampos();
                }
            } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el proveedor: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtIdProveedor.setText("");
        txtNombre.setText("");
        txtContacto.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        tablaProveedores.clearSelection();
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

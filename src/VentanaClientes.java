import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;
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

public class VentanaClientes extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaClientes;
    private JTextField txtIdCliente, txtNombre, txtApellido, txtCedula, txtEmail, txtTelefono, txtDireccion, txtIdSucursal;

    private static final String REGEX_CEDULA = "^\\d{10}$";
    private static final String REGEX_TELEFONO = "^0\\d{9}$";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public VentanaClientes() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosClientes();
    }

    private void initComponents() {
        setTitle("Gestión de Clientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // --- Título Superior ---
        JLabel lblTituloVentana = new JLabel("CLIENTES - Quicentro", JLabel.CENTER);
        lblTituloVentana.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblTituloVentana.setBounds(10, 10, 1160, 30);
        getContentPane().add(lblTituloVentana);

        // --- Panel Izquierdo (Tabla) ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 750, 500);
        getContentPane().add(scrollPane);

        tablaClientes = new JTable();
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaClientes.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaClientes);

        // --- Panel Derecho (Formulario de Datos) ---
        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(245, 245, 245));
        panelDatos.setBounds(780, 50, 380, 500);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos", JLabel.CENTER);
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblDatosTitulo.setBounds(10, 10, 360, 25);
        panelDatos.add(lblDatosTitulo);

        txtIdCliente = crearCampo(panelDatos, "ID Cliente:", 60);
        txtIdCliente.setEditable(false);
        txtNombre = crearCampo(panelDatos, "Nombre:", 110);
        txtApellido = crearCampo(panelDatos, "Apellido:", 160);
        txtCedula = crearCampo(panelDatos, "Cédula:", 210);
        txtEmail = crearCampo(panelDatos, "Email:", 260);
        txtTelefono = crearCampo(panelDatos, "Teléfono:", 310);
        txtDireccion = crearCampo(panelDatos, "Dirección:", 360);
        txtIdSucursal = crearCampo(panelDatos, "ID Sucursal:", 410);

        // --- Panel Inferior (Botonera) ---
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(null);
        panelBotones.setBounds(20, 560, 1140, 80);
        panelBotones.setBackground(new Color(245, 245, 245));
        getContentPane().add(panelBotones);
        
        // --- CORRECCIÓN: Nueva disposición y colores de botones ---
        JButton btnAgregar = crearBoton("Agregar", 120, 10, new Color(144, 238, 144), Color.BLACK);
        JButton btnModificar = crearBoton("Modificar", 290, 10, new Color(255, 255, 0), Color.BLACK);
        JButton btnLimpiar = crearBoton("Limpiar", 460, 10, new Color(173, 216, 230), Color.BLACK); // Azul claro
        JButton btnEliminar = crearBoton("Eliminar", 630, 10, new Color(255, 99, 71), Color.WHITE);
        JButton btnRegresar = crearBoton("Regresar", 950, 10, new Color(255, 165, 0), Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRegresar);

        // --- Action Listeners ---
        btnAgregar.addActionListener(e -> agregarCliente());
        btnModificar.addActionListener(e -> modificarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnRegresar.addActionListener(e -> this.dispose());

        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaClientes.getSelectedRow();
                if (filaSeleccionada != -1) {
                    txtIdCliente.setText(tablaClientes.getValueAt(filaSeleccionada, 0).toString());
                    txtNombre.setText(tablaClientes.getValueAt(filaSeleccionada, 1).toString());
                    txtApellido.setText(tablaClientes.getValueAt(filaSeleccionada, 2).toString());
                    txtCedula.setText(tablaClientes.getValueAt(filaSeleccionada, 3).toString());
                    txtEmail.setText(tablaClientes.getValueAt(filaSeleccionada, 4) != null ? tablaClientes.getValueAt(filaSeleccionada, 4).toString() : "");
                    txtTelefono.setText(tablaClientes.getValueAt(filaSeleccionada, 5) != null ? tablaClientes.getValueAt(filaSeleccionada, 5).toString() : "");
                    txtDireccion.setText(tablaClientes.getValueAt(filaSeleccionada, 6) != null ? tablaClientes.getValueAt(filaSeleccionada, 6).toString() : "");
                    txtIdSucursal.setText(tablaClientes.getValueAt(filaSeleccionada, 7).toString());
                }
            }
        });
    }
    
    private void cargarDatosClientes() {
        String sql = "SELECT idCliente, nombre, apellido, cedula, email, telefono, direccion, idSucursal FROM CLIENTE ORDER BY idCliente";
        tablaClientes.setModel(dbConnector.query(sql));
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty() || txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos Nombre, Apellido y Cédula son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!Pattern.matches(REGEX_CEDULA, txtCedula.getText())) {
            JOptionPane.showMessageDialog(this, "El formato de la Cédula es incorrecto. Debe contener 10 dígitos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!txtTelefono.getText().trim().isEmpty() && !Pattern.matches(REGEX_TELEFONO, txtTelefono.getText())) {
            JOptionPane.showMessageDialog(this, "El formato del Teléfono es incorrecto. Debe empezar con 0 y tener 10 dígitos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!txtEmail.getText().trim().isEmpty() && !Pattern.matches(REGEX_EMAIL, txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "El formato del Email es incorrecto. Use un formato como 'usuario@dominio.com'.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void agregarCliente() {
        if (!validarCampos()) return;
        
        String sql = "INSERT INTO CLIENTE (idCliente, nombre, apellido, cedula, email, telefono, direccion, idSucursal) VALUES ((SELECT NVL(MAX(idCliente), 0) + 1 FROM CLIENTE), ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtApellido.getText());
            pstmt.setString(3, txtCedula.getText());
            pstmt.setString(4, txtEmail.getText());
            pstmt.setString(5, txtTelefono.getText());
            pstmt.setString(6, txtDireccion.getText());
            pstmt.setInt(7, Integer.parseInt(txtIdSucursal.getText()));
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.");
            cargarDatosClientes();
            limpiarCampos();
            
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar el cliente: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarCliente() {
        if (txtIdCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente de la tabla.");
            return;
        }
        if (!validarCampos()) return;
        
        String sql = "UPDATE CLIENTE SET nombre = ?, apellido = ?, cedula = ?, email = ?, telefono = ?, direccion = ?, idSucursal = ? WHERE idCliente = ?";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtApellido.getText());
            pstmt.setString(3, txtCedula.getText());
            pstmt.setString(4, txtEmail.getText());
            pstmt.setString(5, txtTelefono.getText());
            pstmt.setString(6, txtDireccion.getText());
            pstmt.setInt(7, Integer.parseInt(txtIdSucursal.getText()));
            pstmt.setInt(8, Integer.parseInt(txtIdCliente.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                 JOptionPane.showMessageDialog(this, "Cliente modificado exitosamente.");
                 cargarDatosClientes();
                 limpiarCampos();
            } else {
                 JOptionPane.showMessageDialog(this, "No se encontró el cliente para modificar.", "Error de Modificación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar el cliente: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        if (txtIdCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente de la tabla.");
            return;
        }
        int res = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este cliente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM CLIENTE WHERE idCliente = ?";
            try (Connection conn = dbConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, Integer.parseInt(txtIdCliente.getText()));
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                    cargarDatosClientes();
                    limpiarCampos();
                }
            } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el cliente: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtIdCliente.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtIdSucursal.setText("");
        tablaClientes.clearSelection();
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

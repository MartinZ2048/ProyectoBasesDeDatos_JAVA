import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class VentanaClientes extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaClientes;

    // --- Componentes del Formulario ---
    private JTextField txtIdCliente, txtNombre, txtApellido, txtCedula, txtEmail, txtTelefono, txtDireccion, txtIdSucursal;

    // --- Expresiones Regulares para Validación ---
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
        getContentPane().setBackground(new Color(236, 236, 236));

        JLabel lblTitulo = new JLabel("CLIENTES - QUITOCENTRO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBounds(20, 20, 400, 30);
        getContentPane().add(lblTitulo);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 60, 750, 580);
        getContentPane().add(scrollPane);

        tablaClientes = new JTable();
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaClientes.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(0, 122, 204));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaClientes);

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(236, 236, 236));
        panelDatos.setBounds(780, 20, 380, 620);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos del Cliente");
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblDatosTitulo.setBounds(10, 10, 200, 25);
        panelDatos.add(lblDatosTitulo);

        txtIdCliente = crearCampo(panelDatos, "ID Cliente", 60);
        txtIdCliente.setEditable(false);
        txtNombre = crearCampo(panelDatos, "Nombre", 110);
        txtApellido = crearCampo(panelDatos, "Apellido", 160);
        txtCedula = crearCampo(panelDatos, "Cédula", 210);
        txtEmail = crearCampo(panelDatos, "Email", 260);
        txtTelefono = crearCampo(panelDatos, "Teléfono", 310);
        txtDireccion = crearCampo(panelDatos, "Dirección", 360);
        txtIdSucursal = crearCampo(panelDatos, "ID Sucursal", 410);

        JButton btnAgregar = crearBoton("Agregar", 10, 480, new Color(40, 167, 69));
        JButton btnModificar = crearBoton("Modificar", 170, 480, new Color(255, 193, 7));
        JButton btnEliminar = crearBoton("Eliminar", 10, 530, new Color(220, 53, 69));
        JButton btnNuevo = crearBoton("Limpiar", 170, 530, new Color(108, 117, 125));
        JButton btnRegresar = crearBoton("Regresar", 10, 580, new Color(0, 123, 255));
        
        panelDatos.add(btnAgregar);
        panelDatos.add(btnModificar);
        panelDatos.add(btnEliminar);
        panelDatos.add(btnNuevo);
        panelDatos.add(btnRegresar);

        btnNuevo.addActionListener(e -> limpiarCampos());
        btnAgregar.addActionListener(e -> agregarCliente());
        btnModificar.addActionListener(e -> modificarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnRegresar.addActionListener(e -> this.dispose());

        tablaClientes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaClientes.getSelectedRow();
                if (fila != -1) {
                    txtIdCliente.setText(tablaClientes.getValueAt(fila, 0).toString());
                    txtNombre.setText(tablaClientes.getValueAt(fila, 1).toString());
                    txtApellido.setText(tablaClientes.getValueAt(fila, 2).toString());
                    txtCedula.setText(tablaClientes.getValueAt(fila, 3).toString());
                    txtEmail.setText(tablaClientes.getValueAt(fila, 4) != null ? tablaClientes.getValueAt(fila, 4).toString() : "");
                    txtTelefono.setText(tablaClientes.getValueAt(fila, 5) != null ? tablaClientes.getValueAt(fila, 5).toString() : "");
                    txtDireccion.setText(tablaClientes.getValueAt(fila, 6) != null ? tablaClientes.getValueAt(fila, 6).toString() : "");
                    txtIdSucursal.setText(tablaClientes.getValueAt(fila, 7) != null ? tablaClientes.getValueAt(fila, 7).toString() : "");
                }
            }
        });
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
        String datosNuevos = String.format("Nombre: %s, Apellido: %s, Cedula: %s", txtNombre.getText(), txtApellido.getText(), txtCedula.getText());
        String sql = String.format(
            "INSERT INTO CLIENTE (ID_Cliente, Nombre, Apellido, Cedula, Email, Telefono, Direccion, ID_Sucursal) VALUES ((SELECT NVL(MAX(ID_Cliente), 0) + 1 FROM CLIENTE), '%s', '%s', '%s', '%s', '%s', '%s', %s)",
            txtNombre.getText(), txtApellido.getText(), txtCedula.getText(), txtEmail.getText(), txtTelefono.getText(), txtDireccion.getText(), txtIdSucursal.getText()
        );
        if (dbConnector.executeUpdate(sql)) {
            JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.");
            // --- REGISTRO DE AUDITORÍA ---
            dbConnector.registrarAuditoria("CLIENTE", "INSERT", "Nuevo", datosNuevos);
            cargarDatosClientes();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el cliente.");
        }
    }

    private void modificarCliente() {
        if (txtIdCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente de la tabla.");
            return;
        }
        if (!validarCampos()) return;
        String datosNuevos = String.format("Nombre: %s, Apellido: %s, Cedula: %s", txtNombre.getText(), txtApellido.getText(), txtCedula.getText());
        String sql = String.format(
            "UPDATE CLIENTE SET Nombre = '%s', Apellido = '%s', Cedula = '%s', Email = '%s', Telefono = '%s', Direccion = '%s', ID_Sucursal = %s WHERE ID_Cliente = %s",
            txtNombre.getText(), txtApellido.getText(), txtCedula.getText(), txtEmail.getText(), txtTelefono.getText(), txtDireccion.getText(), txtIdSucursal.getText(), txtIdCliente.getText()
        );
        if (dbConnector.executeUpdate(sql)) {
            JOptionPane.showMessageDialog(this, "Cliente modificado exitosamente.");
            // --- REGISTRO DE AUDITORÍA ---
            dbConnector.registrarAuditoria("CLIENTE", "UPDATE", txtIdCliente.getText(), datosNuevos);
            cargarDatosClientes();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar el cliente.");
        }
    }

    private void eliminarCliente() {
        if (txtIdCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente de la tabla.");
            return;
        }
        int res = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este cliente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            String idCliente = txtIdCliente.getText();
            String sql = "DELETE FROM CLIENTE WHERE ID_Cliente = " + idCliente;
            if (dbConnector.executeUpdate(sql)) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                // --- REGISTRO DE AUDITORÍA ---
                dbConnector.registrarAuditoria("CLIENTE", "DELETE", idCliente, "Registro eliminado");
                cargarDatosClientes();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el cliente.");
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
        JLabel lbl = new JLabel(etiqueta + ":");
        lbl.setBounds(10, y, 120, 25);
        panel.add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(140, y, 220, 25);
        panel.add(txt);
        return txt;
    }

    private JButton crearBoton(String texto, int x, int y, Color color) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, 150, 40);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        return boton;
    }

    private void cargarDatosClientes() {
        String sql = "SELECT ID_Cliente, Nombre, Apellido, Cedula, Email, Telefono, Direccion, ID_Sucursal FROM CLIENTE ORDER BY ID_Cliente";
        tablaClientes.setModel(dbConnector.query(sql));
    }
}

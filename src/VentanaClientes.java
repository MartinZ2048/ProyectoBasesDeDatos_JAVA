import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JTextField txtNombres, txtApellidos, txtCedula, txtNombreEmpresa, 
                         txtRuc, txtTelefono, txtCorreo, txtDireccion, txtSede;

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
        scrollPane.setBounds(20, 60, 750, 450);
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

        JLabel lblDatosTitulo = new JLabel("Datos");
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblDatosTitulo.setBounds(10, 10, 100, 25);
        panelDatos.add(lblDatosTitulo);

        // --- Creación de campos del formulario ---
        txtNombres = crearCampo(panelDatos, "Nombres", 60);
        txtApellidos = crearCampo(panelDatos, "Apellidos", 110);
        txtCedula = crearCampo(panelDatos, "C.I.", 160);
        txtCorreo = crearCampo(panelDatos, "Correo Electrónico", 210);
        txtTelefono = crearCampoConPrefijo(panelDatos, "Telef. Celular", 260);
        txtNombreEmpresa = crearCampo(panelDatos, "Nombre de la Empresa", 310);
        txtRuc = crearCampo(panelDatos, "RUC", 360, 140, 100);
        txtSede = crearCampo(panelDatos, "Sucursal", 360, 290, 70);
        txtDireccion = crearCampo(panelDatos, "Dirección", 410);

        // --- BOTONES DE ACCIÓN ---
        JButton btnAgregar = crearBoton("Agregar", 20, 530, new Color(220, 53, 69));
        JButton btnModificar = crearBoton("Modificar", 210, 530, new Color(220, 53, 69));
        JButton btnNuevo = crearBoton("Nuevo Cliente", 400, 530, new Color(220, 53, 69));
        JButton btnEliminar = crearBoton("Eliminar", 590, 530, new Color(220, 53, 69));
        
        getContentPane().add(btnAgregar);
        getContentPane().add(btnModificar);
        getContentPane().add(btnNuevo);
        getContentPane().add(btnEliminar);

        JButton btnRegresar = crearBoton("Regresar", 210, 550, new Color(220, 53, 69));
        panelDatos.add(btnRegresar);

        // --- LÓGICA DE LOS BOTONES ---
        btnNuevo.addActionListener(e -> limpiarCampos());
        btnAgregar.addActionListener(e -> agregarCliente());
        btnModificar.addActionListener(e -> modificarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnRegresar.addActionListener(e -> this.dispose());

        // --- EVENTO PARA SELECCIONAR FILA DE LA TABLA ---
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaClientes.getSelectedRow();
                if (filaSeleccionada != -1) {
                    // El ID (columna 0) no se muestra, pero se usa para UPDATE y DELETE
                    txtNombres.setText(tablaClientes.getValueAt(filaSeleccionada, 1).toString());
                    txtApellidos.setText(tablaClientes.getValueAt(filaSeleccionada, 2).toString());
                    txtCedula.setText(tablaClientes.getValueAt(filaSeleccionada, 3).toString());
                    txtNombreEmpresa.setText(tablaClientes.getValueAt(filaSeleccionada, 4).toString());
                    txtRuc.setText(tablaClientes.getValueAt(filaSeleccionada, 5).toString());
                    txtTelefono.setText(tablaClientes.getValueAt(filaSeleccionada, 6).toString());
                    txtCorreo.setText(tablaClientes.getValueAt(filaSeleccionada, 7).toString());
                    txtDireccion.setText(tablaClientes.getValueAt(filaSeleccionada, 8).toString());
                    txtSede.setText(tablaClientes.getValueAt(filaSeleccionada, 9).toString());
                }
            }
        });
    }

    // --- MÉTODOS DE LÓGICA (CRUD) ---
    private void agregarCliente() {
        if (txtNombres.getText().trim().isEmpty() || txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Cédula son campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sql = String.format(
            "INSERT INTO CLIENTE (IdCliente, nombre, apellido, cedula, nombre_empresarial, ruc, telefono_celular, correo_electronico, direccion, sede) " +
            "VALUES ((SELECT NVL(MAX(IdCliente), 0) + 1 FROM CLIENTE), '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
            txtNombres.getText(), txtApellidos.getText(), txtCedula.getText(), txtNombreEmpresa.getText(),
            txtRuc.getText(), txtTelefono.getText(), txtCorreo.getText(), txtDireccion.getText(), txtSede.getText()
        );
        if (dbConnector.executeUpdate(sql)) {
            JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.");
            cargarDatosClientes();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el cliente.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente de la tabla para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String idCliente = tablaClientes.getValueAt(filaSeleccionada, 0).toString();
        String sql = String.format(
            "UPDATE CLIENTE SET nombre = '%s', apellido = '%s', cedula = '%s', nombre_empresarial = '%s', " +
            "ruc = '%s', telefono_celular = '%s', correo_electronico = '%s', direccion = '%s', sede = '%s' " +
            "WHERE IdCliente = %s",
            txtNombres.getText(), txtApellidos.getText(), txtCedula.getText(), txtNombreEmpresa.getText(),
            txtRuc.getText(), txtTelefono.getText(), txtCorreo.getText(), txtDireccion.getText(),
            txtSede.getText(), idCliente
        );
        if (dbConnector.executeUpdate(sql)) {
            JOptionPane.showMessageDialog(this, "Cliente modificado exitosamente.");
            cargarDatosClientes();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar el cliente.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String idCliente = tablaClientes.getValueAt(filaSeleccionada, 0).toString();
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar a este cliente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM CLIENTE WHERE IdCliente = " + idCliente;
            if (dbConnector.executeUpdate(sql)) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                cargarDatosClientes();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el cliente.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCedula.setText("");
        txtNombreEmpresa.setText("");
        txtRuc.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtSede.setText("");
        tablaClientes.clearSelection();
    }

    // --- Métodos auxiliares para crear componentes ---
    private JTextField crearCampo(JPanel panel, String etiqueta, int yPos) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(10, yPos, 120, 25);
        panel.add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(140, yPos, 220, 25);
        panel.add(txt);
        return txt;
    }
    
    private JTextField crearCampo(JPanel panel, String etiqueta, int yPos, int xPos, int width) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(xPos - 120, yPos, 110, 25);
        panel.add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(xPos, yPos, width, 25);
        panel.add(txt);
        return txt;
    }

    private JTextField crearCampoConPrefijo(JPanel panel, String etiqueta, int yPos) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(10, yPos, 100, 25);
        panel.add(lbl);
        JTextField txtPrefijo = new JTextField("+593");
        txtPrefijo.setEditable(false);
        txtPrefijo.setBounds(140, yPos, 40, 25);
        panel.add(txtPrefijo);
        JTextField txt = new JTextField();
        txt.setBounds(185, yPos, 175, 25);
        panel.add(txt);
        return txt;
    }

    private JButton crearBoton(String texto, int x, int y, Color color) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, 150, 40);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        return boton;
    }

    private void cargarDatosClientes() {
        String sql = "SELECT IdCliente, nombre, apellido, cedula, nombre_empresarial, ruc, telefono_celular, correo_electronico, direccion, sede FROM CLIENTE ORDER BY IdCliente";
        DefaultTableModel model = (DefaultTableModel) dbConnector.query(sql);
        tablaClientes.setModel(model);
    }
}

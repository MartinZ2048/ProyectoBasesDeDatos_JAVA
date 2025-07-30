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

public class VentanaEmpleados extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaEmpleados;
    private JTextField txtIdEmpleado, txtNombre, txtApellido, txtCedula, txtCargo, txtEmail, txtIdSucursal;
    private static final String REGEX_CEDULA = "^\\d{10}$";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public VentanaEmpleados() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosEmpleados();
    }

    private void initComponents() {
        setTitle("Gestión de Empleados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(236, 236, 236));

        JLabel lblTitulo = new JLabel("EMPLEADOS - QUITOCENTRO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBounds(20, 20, 400, 30);
        getContentPane().add(lblTitulo);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 60, 750, 580);
        getContentPane().add(scrollPane);

        tablaEmpleados = new JTable();
        tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaEmpleados.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaEmpleados);

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(236, 236, 236));
        panelDatos.setBounds(780, 20, 380, 620);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos del Empleado");
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblDatosTitulo.setBounds(10, 10, 200, 25);
        panelDatos.add(lblDatosTitulo);

        txtIdEmpleado = crearCampo(panelDatos, "ID Empleado", 60);
        txtIdEmpleado.setEditable(false);
        txtNombre = crearCampo(panelDatos, "Nombre", 110);
        txtApellido = crearCampo(panelDatos, "Apellido", 160);
        txtCedula = crearCampo(panelDatos, "Cédula", 210);
        txtCargo = crearCampo(panelDatos, "Cargo", 260);
        txtEmail = crearCampo(panelDatos, "Email", 310);
        txtIdSucursal = crearCampo(panelDatos, "ID Sucursal", 360);

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
        btnAgregar.addActionListener(e -> agregarEmpleado());
        btnModificar.addActionListener(e -> modificarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
        btnRegresar.addActionListener(e -> this.dispose());

        tablaEmpleados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaEmpleados.getSelectedRow();
                if (fila != -1) {
                    txtIdEmpleado.setText(tablaEmpleados.getValueAt(fila, 0).toString());
                    txtNombre.setText(tablaEmpleados.getValueAt(fila, 1).toString());
                    txtApellido.setText(tablaEmpleados.getValueAt(fila, 2).toString());
                    txtCedula.setText(tablaEmpleados.getValueAt(fila, 3).toString());
                    txtCargo.setText(tablaEmpleados.getValueAt(fila, 4) != null ? tablaEmpleados.getValueAt(fila, 4).toString() : "");
                    txtEmail.setText(tablaEmpleados.getValueAt(fila, 5) != null ? tablaEmpleados.getValueAt(fila, 5).toString() : "");
                    txtIdSucursal.setText(tablaEmpleados.getValueAt(fila, 6) != null ? tablaEmpleados.getValueAt(fila, 6).toString() : "");
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
        if (!txtEmail.getText().trim().isEmpty() && !Pattern.matches(REGEX_EMAIL, txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "El formato del Email es incorrecto. Use un formato como 'usuario@dominio.com'.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void agregarEmpleado() {
        if (!validarCampos()) return;
        String datosNuevos = String.format("Nombre: %s, Apellido: %s, Cedula: %s", txtNombre.getText(), txtApellido.getText(), txtCedula.getText());
        String sql = String.format(
            "INSERT INTO EMPLEADO (ID_Empleado, Nombre, Apellido, Cedula, Cargo, Email, ID_Sucursal) VALUES ((SELECT NVL(MAX(ID_Empleado), 0) + 1 FROM EMPLEADO), '%s', '%s', '%s', '%s', '%s', %s)",
            txtNombre.getText(), txtApellido.getText(), txtCedula.getText(), txtCargo.getText(), txtEmail.getText(), txtIdSucursal.getText()
        );
        if (dbConnector.executeUpdate(sql)) {
            JOptionPane.showMessageDialog(this, "Empleado agregado exitosamente.");
            // --- REGISTRO DE AUDITORÍA ---
            dbConnector.registrarAuditoria("EMPLEADO", "INSERT", "Nuevo", datosNuevos);
            cargarDatosEmpleados();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el empleado.");
        }
    }

    private void modificarEmpleado() {
        if (txtIdEmpleado.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un empleado de la tabla.");
            return;
        }
        if (!validarCampos()) return;
        String datosNuevos = String.format("Nombre: %s, Apellido: %s, Cedula: %s", txtNombre.getText(), txtApellido.getText(), txtCedula.getText());
        String sql = String.format(
            "UPDATE EMPLEADO SET Nombre = '%s', Apellido = '%s', Cedula = '%s', Cargo = '%s', Email = '%s', ID_Sucursal = %s WHERE ID_Empleado = %s",
            txtNombre.getText(), txtApellido.getText(), txtCedula.getText(), txtCargo.getText(), txtEmail.getText(), txtIdSucursal.getText(), txtIdEmpleado.getText()
        );
        if (dbConnector.executeUpdate(sql)) {
            JOptionPane.showMessageDialog(this, "Empleado modificado exitosamente.");
            // --- REGISTRO DE AUDITORÍA ---
            dbConnector.registrarAuditoria("EMPLEADO", "UPDATE", txtIdEmpleado.getText(), datosNuevos);
            cargarDatosEmpleados();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar el empleado.");
        }
    }

    private void eliminarEmpleado() {
        if (txtIdEmpleado.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un empleado de la tabla.");
            return;
        }
        int res = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este empleado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            String idEmpleado = txtIdEmpleado.getText();
            String sql = "DELETE FROM EMPLEADO WHERE ID_Empleado = " + idEmpleado;
            if (dbConnector.executeUpdate(sql)) {
                JOptionPane.showMessageDialog(this, "Empleado eliminado exitosamente.");
                // --- REGISTRO DE AUDITORÍA ---
                dbConnector.registrarAuditoria("EMPLEADO", "DELETE", idEmpleado, "Registro eliminado");
                cargarDatosEmpleados();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el empleado.");
            }
        }
    }

    private void limpiarCampos() {
        txtIdEmpleado.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setText("");
        txtCargo.setText("");
        txtEmail.setText("");
        txtIdSucursal.setText("");
        tablaEmpleados.clearSelection();
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

    private void cargarDatosEmpleados() {
        String sql = "SELECT ID_Empleado, Nombre, Apellido, Cedula, Cargo, Email, ID_Sucursal FROM EMPLEADO ORDER BY ID_Empleado";
        tablaEmpleados.setModel(dbConnector.query(sql));
    }
}

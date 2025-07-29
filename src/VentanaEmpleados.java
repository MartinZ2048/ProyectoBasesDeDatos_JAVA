import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

    // --- Componentes del Formulario (declarados como miembros de la clase) ---
    private JTextField txtIdEmpleado, txtNombres, txtApellidos, txtCedula, txtOcupacion, 
                         txtTelefono, txtCorreo, txtDireccion, txtUsuario, txtClave, 
                         txtSede, txtSueldo;
    private JComboBox<String> comboRol;

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

        // --- Tabla de Empleados (altura ajustada) ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 60, 750, 450);
        getContentPane().add(scrollPane);

        tablaEmpleados = new JTable();
        tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaEmpleados.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaEmpleados);

        // --- Panel de Datos (Formulario a la derecha) ---
        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(236, 236, 236));
        panelDatos.setBounds(780, 20, 380, 620);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos");
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblDatosTitulo.setBounds(10, 10, 100, 25);
        panelDatos.add(lblDatosTitulo);

        // --- Creación de campos del formulario con layout de la imagen ---
        crearCampoFormulario(panelDatos, "Nombres", 40, 220);
        txtNombres = (JTextField) panelDatos.getComponent(panelDatos.getComponentCount() - 1);

        crearCampoFormulario(panelDatos, "Apellidos", 90, 220);
        txtApellidos = (JTextField) panelDatos.getComponent(panelDatos.getComponentCount() - 1);

        crearCampoFormulario(panelDatos, "C.I.", 140, 100);
        txtCedula = (JTextField) panelDatos.getComponent(panelDatos.getComponentCount() - 1);
        
        JLabel lblRol = new JLabel("Rol");
        lblRol.setBounds(250, 140, 40, 25);
        panelDatos.add(lblRol);
        comboRol = new JComboBox<>(new String[]{"Administrador", "Vendedor", "Bodeguero"});
        comboRol.setBounds(280, 140, 80, 25);
        panelDatos.add(comboRol);

        crearCampoFormulario(panelDatos, "Correo Electrónico", 190, 220);
        txtCorreo = (JTextField) panelDatos.getComponent(panelDatos.getComponentCount() - 1);

        JLabel lblTelf = new JLabel("Telef. Celular");
        lblTelf.setBounds(10, 240, 100, 25);
        panelDatos.add(lblTelf);
        JTextField txtTelfPrefijo = new JTextField("+593");
        txtTelfPrefijo.setEditable(false);
        txtTelfPrefijo.setBounds(130, 240, 40, 25);
        panelDatos.add(txtTelfPrefijo);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(175, 240, 185, 25);
        panelDatos.add(txtTelefono);

        crearCampoFormulario(panelDatos, "Ocupación", 290, 125);
        txtOcupacion = (JTextField) panelDatos.getComponent(panelDatos.getComponentCount() - 1);

        JLabel lblSueldo = new JLabel("Sueldo");
        lblSueldo.setBounds(280, 290, 50, 25);
        panelDatos.add(lblSueldo);
        txtSueldo = new JTextField();
        txtSueldo.setBounds(325, 290, 35, 25);
        panelDatos.add(txtSueldo);
        JLabel lblDolar = new JLabel("$");
        lblDolar.setBounds(365, 290, 10, 25);
        panelDatos.add(lblDolar);

        crearCampoFormulario(panelDatos, "Nombre de Usuario", 340, 100);
        txtUsuario = (JTextField) panelDatos.getComponent(panelDatos.getComponentCount() - 1);

        crearCampoFormulario(panelDatos, "Contraseña", 340, 260, 100);
        txtClave = (JTextField) panelDatos.getComponent(panelDatos.getComponentCount() - 1);

        crearCampoFormulario(panelDatos, "Dirección", 390, 100);
        txtDireccion = (JTextField) panelDatos.getComponent(panelDatos.getComponentCount() - 1);

        crearCampoFormulario(panelDatos, "Sucursal", 390, 260, 100);
        txtSede = (JTextField) panelDatos.getComponent(panelDatos.getComponentCount() - 1);

        // --- BOTONES DE ACCIÓN (Ubicación según la imagen) ---
        JButton btnAgregar = crearBoton("Agregar", 20, 530, new Color(220, 53, 69));
        JButton btnModificar = crearBoton("Modificar", 210, 530, new Color(220, 53, 69));
        JButton btnEliminar = crearBoton("Eliminar", 400, 530, new Color(220, 53, 69));
        JButton btnNuevo = crearBoton("Nuevo Empleado", 590, 530, new Color(220, 53, 69));
        
        getContentPane().add(btnAgregar);
        getContentPane().add(btnModificar);
        getContentPane().add(btnEliminar);
        getContentPane().add(btnNuevo);

        JButton btnRegresar = crearBoton("Regresar", 210, 550, new Color(220, 53, 69));
        panelDatos.add(btnRegresar);

        // --- LÓGICA DE LOS BOTONES ---
        btnNuevo.addActionListener(e -> limpiarCampos());
        btnAgregar.addActionListener(e -> agregarEmpleado());
        btnModificar.addActionListener(e -> modificarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
        btnRegresar.addActionListener(e -> this.dispose());

        // --- EVENTO PARA SELECCIONAR FILA DE LA TABLA ---
        tablaEmpleados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaEmpleados.getSelectedRow();
                if (filaSeleccionada != -1) {
                    // El ID no se muestra en el formulario, pero lo necesitamos internamente
                    // Para este ejemplo, asumiremos que no hay un campo txtIdEmpleado visible.
                    // Si lo hubiera, se llenaría aquí.
                    txtNombres.setText(tablaEmpleados.getValueAt(filaSeleccionada, 1).toString());
                    txtApellidos.setText(tablaEmpleados.getValueAt(filaSeleccionada, 2).toString());
                    txtCedula.setText(tablaEmpleados.getValueAt(filaSeleccionada, 3).toString());
                    txtOcupacion.setText(tablaEmpleados.getValueAt(filaSeleccionada, 4).toString());
                    txtTelefono.setText(tablaEmpleados.getValueAt(filaSeleccionada, 5).toString());
                    txtCorreo.setText(tablaEmpleados.getValueAt(filaSeleccionada, 6).toString());
                    txtDireccion.setText(tablaEmpleados.getValueAt(filaSeleccionada, 7).toString());
                    txtUsuario.setText(tablaEmpleados.getValueAt(filaSeleccionada, 8).toString());
                    txtClave.setText(tablaEmpleados.getValueAt(filaSeleccionada, 9).toString());
                    txtSede.setText(tablaEmpleados.getValueAt(filaSeleccionada, 10).toString());
                    txtSueldo.setText(tablaEmpleados.getValueAt(filaSeleccionada, 11).toString());
                }
            }
        });
    }
    
    // --- MÉTODOS DE LÓGICA (CRUD) ---
    private void agregarEmpleado() {
        if (txtNombres.getText().trim().isEmpty() || txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Cédula son campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sql = String.format(
            "INSERT INTO EMPLEADO (IdEmpleado, nombre, apellido, cedula, ocupacion_rol, telefono_celular, correo_electronico, direccion, nombre_usuario, clave_usuario, sede, sueldo) " +
            "VALUES ((SELECT NVL(MAX(IdEmpleado), 0) + 1 FROM EMPLEADO), '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s)",
            txtNombres.getText(), txtApellidos.getText(), txtCedula.getText(), txtOcupacion.getText(),
            txtTelefono.getText(), txtCorreo.getText(), txtDireccion.getText(), txtUsuario.getText(),
            txtClave.getText(), txtSede.getText(), txtSueldo.getText().isEmpty() ? "0" : txtSueldo.getText()
        );
        if (dbConnector.executeUpdate(sql)) {
            JOptionPane.showMessageDialog(this, "Empleado agregado exitosamente.");
            cargarDatosEmpleados();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el empleado.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarEmpleado() {
        int filaSeleccionada = tablaEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un empleado de la tabla para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String idEmpleado = tablaEmpleados.getValueAt(filaSeleccionada, 0).toString();
        String sql = String.format(
            "UPDATE EMPLEADO SET nombre = '%s', apellido = '%s', cedula = '%s', ocupacion_rol = '%s', " +
            "telefono_celular = '%s', correo_electronico = '%s', direccion = '%s', nombre_usuario = '%s', " +
            "clave_usuario = '%s', sede = '%s', sueldo = %s WHERE IdEmpleado = %s",
            txtNombres.getText(), txtApellidos.getText(), txtCedula.getText(), txtOcupacion.getText(),
            txtTelefono.getText(), txtCorreo.getText(), txtDireccion.getText(), txtUsuario.getText(),
            txtClave.getText(), txtSede.getText(), txtSueldo.getText().isEmpty() ? "0" : txtSueldo.getText(),
            idEmpleado
        );
        if (dbConnector.executeUpdate(sql)) {
            JOptionPane.showMessageDialog(this, "Empleado modificado exitosamente.");
            cargarDatosEmpleados();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar el empleado.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarEmpleado() {
        int filaSeleccionada = tablaEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un empleado de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String idEmpleado = tablaEmpleados.getValueAt(filaSeleccionada, 0).toString();
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar a este empleado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM EMPLEADO WHERE IdEmpleado = " + idEmpleado;
            if (dbConnector.executeUpdate(sql)) {
                JOptionPane.showMessageDialog(this, "Empleado eliminado exitosamente.");
                cargarDatosEmpleados();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el empleado.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCedula.setText("");
        txtOcupacion.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtUsuario.setText("");
        txtClave.setText("");
        txtSede.setText("");
        txtSueldo.setText("");
        comboRol.setSelectedIndex(0);
        tablaEmpleados.clearSelection();
    }

    // --- Métodos auxiliares para crear componentes ---
    private void crearCampoFormulario(JPanel panel, String etiqueta, int y, int ancho) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(10, y, 120, 25);
        panel.add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(130, y, ancho, 25);
        panel.add(txt);
    }

    private void crearCampoFormulario(JPanel panel, String etiqueta, int y, int x, int ancho) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(x - 120, y, 110, 25);
        panel.add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(x, y, ancho, 25);
        panel.add(txt);
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

    private void cargarDatosEmpleados() {
        String sql = "SELECT IdEmpleado, nombre, apellido, cedula, ocupacion_rol, telefono_celular, correo_electronico, direccion, nombre_usuario, clave_usuario, sede, sueldo FROM EMPLEADO ORDER BY IdEmpleado";
        DefaultTableModel model = (DefaultTableModel) dbConnector.query(sql);
        tablaEmpleados.setModel(model);
    }
}

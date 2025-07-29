/*
 * =============================================================================
 * --- VentanaEmpleados.java (NUEVA) ---
 * Ventana dedicada para la gestión de Empleados, con un diseño que replica
 * fielmente la imagen proporcionada.
 * =============================================================================
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class VentanaEmpleados extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaEmpleados;

    public VentanaEmpleados() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosEmpleados();
    }

    private void initComponents() {
        // --- CONFIGURACIÓN DE LA VENTANA ---
        setTitle("VentanaEmpleados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(236, 236, 236));

        // --- TÍTULO PRINCIPAL ---
        JLabel lblTitulo = new JLabel("EMPLEADOS - QUITOCENTRO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBounds(20, 20, 400, 30);
        getContentPane().add(lblTitulo);

        // --- TABLA DE EMPLEADOS ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 60, 750, 450);
        getContentPane().add(scrollPane);

        tablaEmpleados = new JTable();
        JTableHeader tableHeader = tablaEmpleados.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60)); // Color oscuro
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaEmpleados);

        // --- PANEL DEL FORMULARIO DE DATOS ---
        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(236, 236, 236));
        panelDatos.setBounds(780, 20, 380, 620);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos");
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblDatosTitulo.setBounds(10, 10, 100, 25);
        panelDatos.add(lblDatosTitulo);

        // --- CAMPOS DEL FORMULARIO ---
        crearCampo(panelDatos, "Nombres", 40);
        crearCampo(panelDatos, "Apellidos", 90);
        crearCampo(panelDatos, "C.I.", 140);
        
        JLabel lblRol = new JLabel("Rol");
        lblRol.setBounds(250, 140, 40, 25);
        panelDatos.add(lblRol);
        JComboBox<String> comboRol = new JComboBox<>(new String[]{"Administrador", "Vendedor", "Bodeguero"});
        comboRol.setBounds(280, 140, 80, 25);
        panelDatos.add(comboRol);

        crearCampo(panelDatos, "Correo Electrónico", 190);
        
        JLabel lblTelf = new JLabel("Telef. Celular");
        lblTelf.setBounds(10, 240, 100, 25);
        panelDatos.add(lblTelf);
        JTextField txtTelfPrefijo = new JTextField("+593");
        txtTelfPrefijo.setEditable(false);
        txtTelfPrefijo.setBounds(130, 240, 40, 25);
        panelDatos.add(txtTelfPrefijo);
        JTextField txtTelf = new JTextField();
        txtTelf.setBounds(175, 240, 185, 25);
        panelDatos.add(txtTelf);

        crearCampo(panelDatos, "Ocupación", 290);
        crearCampoConSimbolo(panelDatos, "Sueldo", 290, "$");
        crearCampo(panelDatos, "Nombre de Usuario", 340);
        crearCampo(panelDatos, "Contraseña", 390);
        crearCampo(panelDatos, "Dirección", 440);
        crearCampo(panelDatos, "Sucursal", 490);

        // --- BOTONES DE ACCIÓN ---
        JButton btnAgregar = crearBoton("Agregar", 20, 530, new Color(220, 53, 69));
        JButton btnModificar = crearBoton("Modificar", 210, 530, new Color(220, 53, 69));
        JButton btnEliminar = crearBoton("Eliminar", 400, 530, new Color(220, 53, 69));
        JButton btnNuevo = crearBoton("Nuevo Empleado", 590, 530, new Color(220, 53, 69));
        getContentPane().add(btnAgregar);
        getContentPane().add(btnModificar);
        getContentPane().add(btnEliminar);
        getContentPane().add(btnNuevo);

        JButton btnRegresar = crearBoton("Regresar", 1010, 580, new Color(220, 53, 69));
        btnRegresar.addActionListener(e -> {
            new VentanaBienvenida().setVisible(true);
            this.dispose();
        });
        panelDatos.add(btnRegresar);
    }
    
    private void crearCampo(JPanel panel, String etiqueta, int yPos) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(10, yPos, 120, 25);
        panel.add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(130, yPos, 230, 25);
        panel.add(txt);
    }
    
    private void crearCampoConSimbolo(JPanel panel, String etiqueta, int yPos, String simbolo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(10, yPos, 120, 25);
        //panel.add(lbl); // Se omite para que no se duplique con el de Ocupación
        JTextField txtSimbolo = new JTextField(simbolo);
        txtSimbolo.setEditable(false);
        txtSimbolo.setBounds(280, yPos, 20, 25);
        panel.add(txtSimbolo);
        JTextField txt = new JTextField();
        txt.setBounds(300, yPos, 60, 25);
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
        String sql = "SELECT nombre || ' ' || apellido AS \"Nombre del Cliente\", cedula AS \"CEDULA\", ocupacion_rol AS \"Ocupación ROL\", telefono_celular AS \"Telefono Celular\", correo_electronico AS \"Correo Electronico\", direccion AS \"Direccion\", nombre_usuario AS \"Nombre de Usuario\", clave_usuario AS \"Clave de Usuario\", sede AS \"SEDE\", sueldo AS \"SUELDO\" FROM EMPLEADO";
        DefaultTableModel model = (DefaultTableModel) dbConnector.query(sql);
        tablaEmpleados.setModel(model);
    }
}

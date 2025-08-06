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
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel lblTituloVentana = new JLabel("EMPLEADOS - Quicentro", JLabel.CENTER);
        lblTituloVentana.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblTituloVentana.setBounds(10, 10, 1160, 30);
        getContentPane().add(lblTituloVentana);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 750, 500);
        getContentPane().add(scrollPane);

        tablaEmpleados = new JTable();
        tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaEmpleados.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaEmpleados);

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(245, 245, 245));
        panelDatos.setBounds(780, 50, 380, 500);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos", JLabel.CENTER);
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblDatosTitulo.setBounds(10, 10, 360, 25);
        panelDatos.add(lblDatosTitulo);

        txtIdEmpleado = crearCampo(panelDatos, "ID Empleado:", 60);
        txtIdEmpleado.setEditable(false);
        txtNombre = crearCampo(panelDatos, "Nombre:", 110);
        txtApellido = crearCampo(panelDatos, "Apellido:", 160);
        txtCedula = crearCampo(panelDatos, "Cédula:", 210);
        txtCargo = crearCampo(panelDatos, "Cargo:", 260);
        txtEmail = crearCampo(panelDatos, "Email:", 310);
        txtIdSucursal = crearCampo(panelDatos, "ID Sucursal:", 360);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(null);
        panelBotones.setBounds(20, 560, 1140, 80);
        panelBotones.setBackground(new Color(245, 245, 245));
        getContentPane().add(panelBotones);
        
        JButton btnAgregar = crearBoton("Agregar", 120, 10, new Color(144, 238, 144), Color.BLACK);
        JButton btnModificar = crearBoton("Modificar", 290, 10, new Color(255, 255, 0), Color.BLACK);
        JButton btnLimpiar = crearBoton("Limpiar", 460, 10, new Color(173, 216, 230), Color.BLACK);
        JButton btnEliminar = crearBoton("Eliminar", 630, 10, new Color(255, 99, 71), Color.WHITE);
        JButton btnRegresar = crearBoton("Regresar", 950, 10, new Color(255, 165, 0), Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRegresar);

        btnAgregar.addActionListener(e -> agregarEmpleado());
        btnModificar.addActionListener(e -> modificarEmpleado());
        btnLimpiar.addActionListener(e -> limpiarCampos());
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
                    txtIdSucursal.setText(tablaEmpleados.getValueAt(fila, 6).toString());
                }
            }
        });
    }
    
    private void cargarDatosEmpleados() {
        String sql = "SELECT idEmpleado, nombre, apellido, cedula, cargo, email, idSucursal FROM EMPLEADO ORDER BY idEmpleado";
        tablaEmpleados.setModel(dbConnector.query(sql));
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
        
        String sql = "INSERT INTO EMPLEADO (idEmpleado, nombre, apellido, cedula, cargo, email, idSucursal) VALUES ((SELECT NVL(MAX(idEmpleado), 0) + 1 FROM EMPLEADO), ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtApellido.getText());
            pstmt.setString(3, txtCedula.getText());
            pstmt.setString(4, txtCargo.getText());
            pstmt.setString(5, txtEmail.getText());
            pstmt.setInt(6, Integer.parseInt(txtIdSucursal.getText()));
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Empleado agregado exitosamente.");
            cargarDatosEmpleados();
            limpiarCampos();

        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar el empleado: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarEmpleado() {
        if (txtIdEmpleado.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un empleado de la tabla.");
            return;
        }
        if (!validarCampos()) return;
        
        String sql = "UPDATE EMPLEADO SET nombre = ?, apellido = ?, cedula = ?, cargo = ?, email = ?, idSucursal = ? WHERE idEmpleado = ?";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtApellido.getText());
            pstmt.setString(3, txtCedula.getText());
            pstmt.setString(4, txtCargo.getText());
            pstmt.setString(5, txtEmail.getText());
            pstmt.setInt(6, Integer.parseInt(txtIdSucursal.getText()));
            pstmt.setInt(7, Integer.parseInt(txtIdEmpleado.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                 JOptionPane.showMessageDialog(this, "Empleado modificado exitosamente.");
                 cargarDatosEmpleados();
                 limpiarCampos();
            } else {
                 JOptionPane.showMessageDialog(this, "No se encontró el empleado para modificar.", "Error de Modificación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar el empleado: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEmpleado() {
        if (txtIdEmpleado.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un empleado de la tabla.");
            return;
        }
        int res = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este empleado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM EMPLEADO WHERE idEmpleado = ?";
            try (Connection conn = dbConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, Integer.parseInt(txtIdEmpleado.getText()));
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Empleado eliminado exitosamente.");
                    cargarDatosEmpleados();
                    limpiarCampos();
                }
            } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el empleado: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
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

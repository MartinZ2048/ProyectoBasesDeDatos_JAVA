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

public class VentanaSucursales extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaSucursales;
    private JTextField txtIdSucursal, txtNombre, txtCiudad, txtTelefono, txtDireccion;

    public VentanaSucursales() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosSucursales();
    }

    private void initComponents() {
        setTitle("Gestión de Sucursales");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel lblTituloVentana = new JLabel("\"SUCURSALES\" - Quicentro", JLabel.CENTER);
        lblTituloVentana.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblTituloVentana.setBounds(10, 10, 1160, 30);
        getContentPane().add(lblTituloVentana);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 750, 500);
        getContentPane().add(scrollPane);

        tablaSucursales = new JTable();
        tablaSucursales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaSucursales.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaSucursales);

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(245, 245, 245));
        panelDatos.setBounds(780, 50, 380, 500);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos", JLabel.CENTER);
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblDatosTitulo.setBounds(10, 10, 360, 25);
        panelDatos.add(lblDatosTitulo);

        txtIdSucursal = crearCampo(panelDatos, "ID Sucursal:", 60);
        txtIdSucursal.setEditable(false);
        txtNombre = crearCampo(panelDatos, "Nombre:", 110);
        txtCiudad = crearCampo(panelDatos, "Ciudad:", 160);
        txtTelefono = crearCampo(panelDatos, "Teléfono:", 210);
        txtDireccion = crearCampo(panelDatos, "Dirección:", 260);

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

        btnAgregar.addActionListener(e -> agregarSucursal());
        btnModificar.addActionListener(e -> modificarSucursal());
        btnEliminar.addActionListener(e -> eliminarSucursal());
        btnRegresar.addActionListener(e -> this.dispose());

        tablaSucursales.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaSucursales.getSelectedRow();
                if (filaSeleccionada != -1) {
                    txtIdSucursal.setText(tablaSucursales.getValueAt(filaSeleccionada, 0).toString());
                    txtNombre.setText(tablaSucursales.getValueAt(filaSeleccionada, 1).toString());
                    txtCiudad.setText(tablaSucursales.getValueAt(filaSeleccionada, 2) != null ? tablaSucursales.getValueAt(filaSeleccionada, 2).toString() : "");
                    txtTelefono.setText(tablaSucursales.getValueAt(filaSeleccionada, 3) != null ? tablaSucursales.getValueAt(filaSeleccionada, 3).toString() : "");
                    txtDireccion.setText(tablaSucursales.getValueAt(filaSeleccionada, 4) != null ? tablaSucursales.getValueAt(filaSeleccionada, 4).toString() : "");
                }
            }
        });
    }

    private void cargarDatosSucursales() {
        String sql = "SELECT idSucursal, nombre, ciudad, telefono, direccion FROM SUCURSAL ORDER BY idSucursal";
        tablaSucursales.setModel(dbConnector.query(sql));
    }

    private void agregarSucursal() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de la sucursal es obligatorio.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO SUCURSAL (idSucursal, nombre, ciudad, telefono, direccion) VALUES ((SELECT NVL(MAX(idSucursal), 0) + 1 FROM SUCURSAL), ?, ?, ?, ?)";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtCiudad.getText());
            pstmt.setString(3, txtTelefono.getText());
            pstmt.setString(4, txtDireccion.getText());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Sucursal agregada exitosamente.");
                cargarDatosSucursales();
                limpiarCampos();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar la sucursal: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarSucursal() {
        if (txtIdSucursal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una sucursal de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE SUCURSAL SET nombre = ?, ciudad = ?, telefono = ?, direccion = ? WHERE idSucursal = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtCiudad.getText());
            pstmt.setString(3, txtTelefono.getText());
            pstmt.setString(4, txtDireccion.getText());
            pstmt.setInt(5, Integer.parseInt(txtIdSucursal.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Sucursal modificada exitosamente.");
                cargarDatosSucursales();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la sucursal para modificar.", "Error de Modificación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar la sucursal: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSucursal() {
        if (txtIdSucursal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una sucursal de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar esta sucursal?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM SUCURSAL WHERE idSucursal = ?";
            try (Connection conn = dbConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, Integer.parseInt(txtIdSucursal.getText()));

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Sucursal eliminada exitosamente.");
                    cargarDatosSucursales();
                    limpiarCampos();
                }
            } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar la sucursal. Verifique que no esté siendo usada por clientes o empleados.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtIdSucursal.setText("");
        txtNombre.setText("");
        txtCiudad.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        tablaSucursales.clearSelection();
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

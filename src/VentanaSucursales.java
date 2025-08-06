import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

public class VentanaSucursales extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaSucursales;

    // --- NUEVO: Campos para modificar ---
    private JTextField txtNombre, txtCiudad, txtTelefono, txtDireccion;
    private JLabel lblIdSucursal; // Para saber qué sucursal modificar

    public VentanaSucursales() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosSucursales();
    }

    private void initComponents() {
        setTitle("Sucursales");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTituloVentana = new JLabel("Sucursal - Quicentro", SwingConstants.CENTER);
        lblTituloVentana.setFont(new Font("Monospaced", Font.BOLD, 24));
        add(lblTituloVentana, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        tablaSucursales = new JTable();
        tablaSucursales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaSucursales.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaSucursales);
        
        // --- Evento para seleccionar una fila y poder modificarla ---
        tablaSucursales.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaSucursales.getSelectedRow();
                if (filaSeleccionada != -1) {
                    // Guardamos el ID para la consulta UPDATE
                    lblIdSucursal.setText(tablaSucursales.getValueAt(filaSeleccionada, 0).toString());
                    
                    // Mostramos los datos en campos de texto para edición
                    txtNombre.setText(tablaSucursales.getValueAt(filaSeleccionada, 1).toString());
                    txtCiudad.setText(tablaSucursales.getValueAt(filaSeleccionada, 2) != null ? tablaSucursales.getValueAt(filaSeleccionada, 2).toString() : "");
                    txtTelefono.setText(tablaSucursales.getValueAt(filaSeleccionada, 3) != null ? tablaSucursales.getValueAt(filaSeleccionada, 3).toString() : "");
                    txtDireccion.setText(tablaSucursales.getValueAt(filaSeleccionada, 4) != null ? tablaSucursales.getValueAt(filaSeleccionada, 4).toString() : "");
                }
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(245, 245, 245));
        add(panelBotones, BorderLayout.SOUTH);
        
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(255, 165, 0));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegresar.setPreferredSize(new Dimension(150, 40));
        btnRegresar.addActionListener(e -> this.dispose());
        
        // --- NUEVO: Botón Modificar ---
        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBackground(new Color(255, 255, 0)); // Amarillo
        btnModificar.setForeground(Color.BLACK);
        btnModificar.setFont(new Font("Arial", Font.BOLD, 14));
        btnModificar.setPreferredSize(new Dimension(150, 40));
        btnModificar.addActionListener(e -> modificarSucursal());

        panelBotones.add(btnRegresar);
        panelBotones.add(btnModificar);
        
        // --- NUEVO: Panel para los campos de edición ---
        JPanel panelEdicion = new JPanel();
        panelEdicion.setLayout(null);
        panelEdicion.setPreferredSize(new Dimension(0, 100)); // Altura para los campos
        panelEdicion.setBackground(new Color(245, 245, 245));
        
        lblIdSucursal = new JLabel(); // No visible, solo para guardar el ID
        txtNombre = crearCampo(panelEdicion, "Nombre:", 10, 10, 150);
        txtCiudad = crearCampo(panelEdicion, "Ciudad:", 10, 40, 150);
        txtTelefono = crearCampo(panelEdicion, "Teléfono:", 350, 10, 150);
        txtDireccion = crearCampo(panelEdicion, "Dirección:", 350, 40, 150);
        
        add(panelEdicion, BorderLayout.WEST); // Añadimos el panel al oeste (izquierda)
    }
    
    private JTextField crearCampo(JPanel panel, String etiqueta, int x, int y, int ancho) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(x, y, 80, 25);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(x + 85, y, ancho, 25);
        panel.add(txt);
        return txt;
    }

    private void cargarDatosSucursales() {
        String sql = "SELECT idSucursal, nombre, ciudad, telefono, direccion FROM SUCURSAL ORDER BY idSucursal";
        tablaSucursales.setModel(dbConnector.query(sql));
    }
    
    private void modificarSucursal() {
        if (lblIdSucursal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una sucursal de la tabla para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE SUCURSAL SET nombre = ?, ciudad = ?, telefono = ?, direccion = ? WHERE idSucursal = ?";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtCiudad.getText());
            pstmt.setString(3, txtTelefono.getText());
            pstmt.setString(4, txtDireccion.getText());
            pstmt.setInt(5, Integer.parseInt(lblIdSucursal.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Sucursal modificada exitosamente.");
                cargarDatosSucursales(); // Recargar la tabla
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la sucursal para modificar.", "Error de Modificación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar la sucursal: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCampos() {
        lblIdSucursal.setText("");
        txtNombre.setText("");
        txtCiudad.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        tablaSucursales.clearSelection();
    }
}

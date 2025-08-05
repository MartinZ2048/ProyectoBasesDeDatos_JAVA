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

public class VentanaSuministro extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaSuministros;
    private JTextField txtIdSuministro, txtIdProveedor, txtIdLibro, txtCantidad, txtFecha;

    public VentanaSuministro() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosSuministros();
    }

    private void initComponents() {
        setTitle("Gestión de Suministros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel lblTituloVentana = new JLabel("\"SUMINISTRO\" - Quicentro", JLabel.CENTER);
        lblTituloVentana.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblTituloVentana.setBounds(10, 10, 1160, 30);
        getContentPane().add(lblTituloVentana);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 750, 500);
        getContentPane().add(scrollPane);

        tablaSuministros = new JTable();
        tablaSuministros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaSuministros.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaSuministros);

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(245, 245, 245));
        panelDatos.setBounds(780, 50, 380, 500);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos", JLabel.CENTER);
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblDatosTitulo.setBounds(10, 10, 360, 25);
        panelDatos.add(lblDatosTitulo);

        txtIdSuministro = crearCampo(panelDatos, "ID Suministro:", 60);
        txtIdSuministro.setEditable(false);
        txtIdProveedor = crearCampo(panelDatos, "ID Proveedor:", 110);
        txtIdLibro = crearCampo(panelDatos, "ID Libro:", 160);
        txtCantidad = crearCampo(panelDatos, "Cantidad:", 210);
        txtFecha = crearCampo(panelDatos, "Fecha:", 260);
        txtFecha.setEditable(false);

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

        btnAgregar.addActionListener(e -> agregarSuministro());
        btnModificar.addActionListener(e -> modificarSuministro());
        btnEliminar.addActionListener(e -> eliminarSuministro());
        btnRegresar.addActionListener(e -> this.dispose());

        tablaSuministros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaSuministros.getSelectedRow();
                if (filaSeleccionada != -1) {
                    txtIdSuministro.setText(tablaSuministros.getValueAt(filaSeleccionada, 0).toString());
                    txtIdProveedor.setText(tablaSuministros.getValueAt(filaSeleccionada, 1) != null ? tablaSuministros.getValueAt(filaSeleccionada, 1).toString() : "");
                    txtIdLibro.setText(tablaSuministros.getValueAt(filaSeleccionada, 2) != null ? tablaSuministros.getValueAt(filaSeleccionada, 2).toString() : "");
                    txtCantidad.setText(tablaSuministros.getValueAt(filaSeleccionada, 3) != null ? tablaSuministros.getValueAt(filaSeleccionada, 3).toString() : "");
                    txtFecha.setText(tablaSuministros.getValueAt(filaSeleccionada, 4) != null ? tablaSuministros.getValueAt(filaSeleccionada, 4).toString() : "");
                }
            }
        });
    }

    private void cargarDatosSuministros() {
        String sql = "SELECT idSuministro, idProveedor, idLibro, cantidad, TO_CHAR(fecha, 'YYYY-MM-DD HH24:MI:SS') as fecha FROM SUMINISTRO ORDER BY idSuministro";
        tablaSuministros.setModel(dbConnector.query(sql));
    }

    private void agregarSuministro() {
        if (txtIdProveedor.getText().trim().isEmpty() || txtIdLibro.getText().trim().isEmpty() || txtCantidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Proveedor, ID Libro y Cantidad son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO SUMINISTRO (idSuministro, idProveedor, idLibro, cantidad, fecha) VALUES ((SELECT NVL(MAX(idSuministro), 0) + 1 FROM SUMINISTRO), ?, ?, ?, SYSDATE)";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(txtIdProveedor.getText()));
            pstmt.setInt(2, Integer.parseInt(txtIdLibro.getText()));
            pstmt.setInt(3, Integer.parseInt(txtCantidad.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Suministro agregado exitosamente.");
                cargarDatosSuministros();
                limpiarCampos();
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar el suministro: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarSuministro() {
        if (txtIdSuministro.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un suministro de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE SUMINISTRO SET idProveedor = ?, idLibro = ?, cantidad = ? WHERE idSuministro = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtIdProveedor.getText()));
            pstmt.setInt(2, Integer.parseInt(txtIdLibro.getText()));
            pstmt.setInt(3, Integer.parseInt(txtCantidad.getText()));
            pstmt.setInt(4, Integer.parseInt(txtIdSuministro.getText()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Suministro modificado exitosamente.");
                cargarDatosSuministros();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el suministro para modificar.", "Error de Modificación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar el suministro: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSuministro() {
        if (txtIdSuministro.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un suministro de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este suministro?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM SUMINISTRO WHERE idSuministro = ?";
            try (Connection conn = dbConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, Integer.parseInt(txtIdSuministro.getText()));

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Suministro eliminado exitosamente.");
                    cargarDatosSuministros();
                    limpiarCampos();
                }
            } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el suministro: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limpiarCampos() {
        txtIdSuministro.setText("");
        txtIdProveedor.setText("");
        txtIdLibro.setText("");
        txtCantidad.setText("");
        txtFecha.setText("");
        tablaSuministros.clearSelection();
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

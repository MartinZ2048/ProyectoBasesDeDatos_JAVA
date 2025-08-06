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

public class VentanaLibros extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaLibros;

    // --- Componentes del Formulario (con el nuevo campo idSucursal) ---
    private JTextField txtIdLibro, txtTitulo, txtIsbn, txtAutor, txtEditorial, txtPrecio, txtIdSucursal;

    public VentanaLibros() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosLibros();
    }

    private void initComponents() {
        setTitle("Gestión de Libros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // --- Título Superior (ajustado a la imagen de referencia) ---
        JLabel lblTituloVentana = new JLabel("Libros - Quicentro", JLabel.CENTER);
        lblTituloVentana.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblTituloVentana.setBounds(10, 10, 1160, 30);
        getContentPane().add(lblTituloVentana);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 50, 750, 500);
        getContentPane().add(scrollPane);

        tablaLibros = new JTable();
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaLibros.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaLibros);

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(new Color(245, 245, 245));
        panelDatos.setBounds(780, 50, 380, 500);
        panelDatos.setLayout(null);
        getContentPane().add(panelDatos);

        JLabel lblDatosTitulo = new JLabel("Datos", JLabel.CENTER);
        lblDatosTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblDatosTitulo.setBounds(10, 10, 360, 25);
        panelDatos.add(lblDatosTitulo);

        // --- Creación de campos de texto, incluyendo idSucursal ---
        txtIdLibro = crearCampo(panelDatos, "ID Libro:", 60);
        txtIdLibro.setEditable(false);
        txtTitulo = crearCampo(panelDatos, "Título:", 110);
        txtIsbn = crearCampo(panelDatos, "ISBN:", 160);
        txtAutor = crearCampo(panelDatos, "Autor:", 210);
        txtEditorial = crearCampo(panelDatos, "Editorial:", 260);
        txtPrecio = crearCampo(panelDatos, "Precio:", 310);
        txtIdSucursal = crearCampo(panelDatos, "ID Sucursal:", 360); // <-- NUEVO CAMPO

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

        btnAgregar.addActionListener(e -> agregarLibro());
        btnModificar.addActionListener(e -> modificarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnRegresar.addActionListener(e -> this.dispose());

        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaLibros.getSelectedRow();
                if (filaSeleccionada != -1) {
                    txtIdLibro.setText(tablaLibros.getValueAt(filaSeleccionada, 0).toString());
                    txtTitulo.setText(tablaLibros.getValueAt(filaSeleccionada, 1).toString());
                    txtIsbn.setText(tablaLibros.getValueAt(filaSeleccionada, 2) != null ? tablaLibros.getValueAt(filaSeleccionada, 2).toString() : "");
                    txtAutor.setText(tablaLibros.getValueAt(filaSeleccionada, 3) != null ? tablaLibros.getValueAt(filaSeleccionada, 3).toString() : "");
                    txtEditorial.setText(tablaLibros.getValueAt(filaSeleccionada, 4) != null ? tablaLibros.getValueAt(filaSeleccionada, 4).toString() : "");
                    txtPrecio.setText(tablaLibros.getValueAt(filaSeleccionada, 5) != null ? tablaLibros.getValueAt(filaSeleccionada, 5).toString() : "");
                    txtIdSucursal.setText(tablaLibros.getValueAt(filaSeleccionada, 6).toString()); // <-- OBTENER NUEVO CAMPO
                }
            }
        });
    }

    private void cargarDatosLibros() {
        // --- CORRECCIÓN: La consulta ahora incluye idSucursal ---
        String sql = "SELECT idLibro, titulo, isbn, autor, editorial, precio, idSucursal FROM LIBRO ORDER BY idLibro";
        tablaLibros.setModel(dbConnector.query(sql));
    }

    private void agregarLibro() {
        if (txtTitulo.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty() || txtIdSucursal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título, Precio e ID Sucursal son campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- CORRECCIÓN: La consulta INSERT ahora incluye idSucursal ---
        String sql = "INSERT INTO LIBRO (idLibro, titulo, isbn, autor, editorial, precio, idSucursal) VALUES ((SELECT NVL(MAX(idLibro), 0) + 1 FROM LIBRO), ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = dbConnector.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtTitulo.getText());
                pstmt.setString(2, txtIsbn.getText());
                pstmt.setString(3, txtAutor.getText());
                pstmt.setString(4, txtEditorial.getText());
                pstmt.setDouble(5, Double.parseDouble(txtPrecio.getText()));
                pstmt.setInt(6, Integer.parseInt(txtIdSucursal.getText()));

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit(); // --- Realizar COMMIT explícito ---
                    JOptionPane.showMessageDialog(this, "Libro agregado exitosamente.");
                    cargarDatosLibros();
                    limpiarCampos();
                } else {
                    conn.rollback();
                }
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            try { if (conn != null) conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            JOptionPane.showMessageDialog(this, "Error al agregar el libro: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void modificarLibro() {
        if (txtIdLibro.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un libro de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- CORRECCIÓN: La consulta UPDATE ahora incluye idSucursal ---
        String sql = "UPDATE LIBRO SET titulo = ?, isbn = ?, autor = ?, editorial = ?, precio = ?, idSucursal = ? WHERE idLibro = ?";
        Connection conn = null;
        try {
            conn = dbConnector.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtTitulo.getText());
                pstmt.setString(2, txtIsbn.getText());
                pstmt.setString(3, txtAutor.getText());
                pstmt.setString(4, txtEditorial.getText());
                pstmt.setDouble(5, Double.parseDouble(txtPrecio.getText()));
                pstmt.setInt(6, Integer.parseInt(txtIdSucursal.getText()));
                pstmt.setInt(7, Integer.parseInt(txtIdLibro.getText()));

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit(); // --- Realizar COMMIT explícito ---
                    JOptionPane.showMessageDialog(this, "Libro modificado exitosamente.");
                    cargarDatosLibros();
                    limpiarCampos();
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "No se encontró el libro para modificar.", "Error de Modificación", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            try { if (conn != null) conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            JOptionPane.showMessageDialog(this, "Error al modificar el libro: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void eliminarLibro() {
        if (txtIdLibro.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un libro de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este libro?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM LIBRO WHERE idLibro = ?";
            Connection conn = null;
            try {
                conn = dbConnector.getConnection();
                conn.setAutoCommit(false);
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(txtIdLibro.getText()));
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        conn.commit(); // --- Realizar COMMIT explícito ---
                        JOptionPane.showMessageDialog(this, "Libro eliminado exitosamente.");
                        cargarDatosLibros();
                        limpiarCampos();
                    } else {
                        conn.rollback();
                    }
                }
            } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
                try { if (conn != null) conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
                JOptionPane.showMessageDialog(this, "Error al eliminar el libro: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            } finally {
                try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private void limpiarCampos() {
        txtIdLibro.setText("");
        txtTitulo.setText("");
        txtIsbn.setText("");
        txtAutor.setText("");
        txtEditorial.setText("");
        txtPrecio.setText("");
        txtIdSucursal.setText(""); // <-- LIMPIAR NUEVO CAMPO
        tablaLibros.clearSelection();
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

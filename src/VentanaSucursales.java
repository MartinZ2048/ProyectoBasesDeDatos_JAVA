import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

public class VentanaSucursales extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaSucursales;

    public VentanaSucursales() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosSucursales();
    }

    private void initComponents() {
        setTitle("Sucursales");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600); // Tamaño ajustado para la nueva disposición
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
        // Se usa un JPanel como contentPane para poder añadirle un borde
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Título Superior (ajustado a la imagen de referencia) ---
        JLabel lblTituloVentana = new JLabel("Sucursal - Quicentro", SwingConstants.CENTER);
        lblTituloVentana.setFont(new Font("Monospaced", Font.BOLD, 24));
        add(lblTituloVentana, BorderLayout.NORTH);

        // --- Panel Central (Tabla) ---
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        tablaSucursales = new JTable();
        tablaSucursales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = tablaSucursales.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(60, 60, 60));
        tableHeader.setForeground(Color.WHITE);
        scrollPane.setViewportView(tablaSucursales);

        // --- Panel Inferior (Botonera) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBackground(new Color(245, 245, 245));
        panelBotones.setBorder(new EmptyBorder(10, 0, 0, 0));
        add(panelBotones, BorderLayout.SOUTH);
        
        // --- Único Botón "Regresar" ---
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(255, 165, 0));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegresar.setPreferredSize(new Dimension(150, 40));
        btnRegresar.addActionListener(e -> this.dispose());
        panelBotones.add(btnRegresar);
    }

    private void cargarDatosSucursales() {
        String sql = "SELECT idSucursal, nombre, ciudad, telefono, direccion FROM SUCURSAL ORDER BY idSucursal";
        tablaSucursales.setModel(dbConnector.query(sql));
    }
}

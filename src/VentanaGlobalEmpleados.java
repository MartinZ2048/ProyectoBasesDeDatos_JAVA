import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

public class VentanaGlobalEmpleados extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaDatos;
    private final String sqlQuery = "SELECT * FROM empleado_gb";
    private final String windowName = "Empleados";

    public VentanaGlobalEmpleados() {
        this.dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("VISTA GLOBAL - " + windowName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(new Color(236, 236, 236));
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("VISTA GLOBAL - \"" + windowName + "\" - Quicentro", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 24));
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new LineBorder(Color.GRAY));
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(tablePanel, BorderLayout.CENTER);

        tablaDatos = new JTable();
        JTableHeader tableHeader = tablaDatos.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setForeground(Color.BLACK);
        scrollPane.setViewportView(tablaDatos);
        
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        panelSur.setBackground(new Color(236, 236, 236));
        contentPane.add(panelSur, BorderLayout.SOUTH);

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(255, 165, 0));
        btnRegresar.setForeground(Color.BLACK);
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegresar.setPreferredSize(new Dimension(150, 40));
        btnRegresar.setBorder(new LineBorder(Color.BLACK, 1));
        btnRegresar.setFocusPainted(false);
        btnRegresar.addActionListener(e -> this.dispose());
        panelSur.add(btnRegresar);
    }

    private void cargarDatos() {
        tablaDatos.setModel(dbConnector.query(sqlQuery));
    }
}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

public class VentanaAuditoria extends JFrame {

    private final DatabaseConnector dbConnector;
    private JTable tablaAuditoria;

    public VentanaAuditoria() {
        dbConnector = new DatabaseConnector();
        initComponents();
        cargarDatosAuditoria();
    }

    private void initComponents() {
        setTitle("Ventana Auditorias");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(236, 236, 236));
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("Auditorias MATRIZ", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        tablaAuditoria = new JTable();
        JTableHeader tableHeader = tablaAuditoria.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setForeground(Color.BLACK);
        scrollPane.setViewportView(tablaAuditoria);
        
        JPanel panelSur = new JPanel();
        panelSur.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelSur.setBackground(new Color(236, 236, 236));
        add(panelSur, BorderLayout.SOUTH);

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(220, 53, 69));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegresar.setPreferredSize(new Dimension(150, 40));
        btnRegresar.addActionListener(e -> this.dispose());
        panelSur.add(btnRegresar);
    }

    private void cargarDatosAuditoria() {
        // Consulta para mostrar los datos de auditoría más recientes primero
        String sql = "SELECT usuario AS \"USER_NAME\", " +
                     "TO_CHAR(fecha, 'YYYY-MM-DD HH24:MI:SS') AS \"FECHA\", " +
                     "operacion AS \"TIPO_OPERACION\", " +
                     "tabla_afectada AS \"NOMBRE_TABLE\", " +
                     "id_registro AS \"ID_REGISTRO\" " +
                     "FROM AUDITORIA ORDER BY fecha DESC";
        tablaAuditoria.setModel(dbConnector.query(sql));
    }
}

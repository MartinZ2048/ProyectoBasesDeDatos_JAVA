/*
 * =============================================================================
 * --- VentanaPrincipal.java (VERSIÓN FINAL Y CORREGIDA) ---
 * Llama al conector de la base de datos y muestra los resultados
 * en tablas dinámicas al hacer clic en los botones del menú.
 * =============================================================================
 */
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

public class VentanaPrincipal extends JFrame {

    private final JPanel panelContenido;
    private final CardLayout cardLayout;
    private final DatabaseConnector dbConnector;

    public VentanaPrincipal() {
        dbConnector = new DatabaseConnector();

        setTitle("Biblioteca Virtual - Panel Principal");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(1, 1));
        getContentPane().setBackground(new Color(220, 220, 220));

        JPanel panelMenu = crearPanelMenu();
        add(panelMenu, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        
        panelContenido.add(crearPanelDeBienvenida(), "Bienvenida");
        add(panelContenido, BorderLayout.CENTER);
        
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private JPanel crearPanelMenu() {
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setPreferredSize(new Dimension(220, 0));
        panelMenu.setBackground(new Color(245, 245, 245));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTituloMenu = new JLabel("BIBLIOTECA-EPN");
        lblTituloMenu.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTituloMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTituloMenu.setBorder(BorderFactory.createEmptyBorder(0, 5, 20, 0));
        panelMenu.add(lblTituloMenu);

        String[] modulos = {"Catálogo", "Proveedores", "Sucursales", "Clientes", "Empleados", "Ventas", "Auditoría"};

        for (String modulo : modulos) {
            JButton boton = new JButton(modulo);
            boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            boton.addActionListener(e -> mostrarPanel(modulo));
            panelMenu.add(boton);
            panelMenu.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return panelMenu;
    }

    private void mostrarPanel(String nombrePanel) {
        String sql;
        switch (nombrePanel) {
            case "Catálogo":
                sql = "SELECT IdLibro, titulo, autor, editorial, precio FROM LIBRO ORDER BY titulo";
                break;
            case "Proveedores":
                sql = "SELECT * FROM PROVEEDOR ORDER BY nombre";
                break;
            case "Sucursales":
                sql = "SELECT * FROM SUCURSAL ORDER BY ciudad, nombre";
                break;
            default:
                JPanel panelNoImplementado = crearPanelGenerico(nombrePanel, "Módulo no implementado.");
                panelContenido.add(panelNoImplementado, nombrePanel);
                cardLayout.show(panelContenido, nombrePanel);
                return;
        }

        TableModel tableModel = dbConnector.query(sql);
        JPanel panelConTabla = crearPanelConTabla(nombrePanel, tableModel);
        
        panelContenido.add(panelConTabla, nombrePanel);
        cardLayout.show(panelContenido, nombrePanel);
    }

    private JPanel crearPanelConTabla(String titulo, TableModel model) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Módulo de " + titulo, SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Roboto", Font.BOLD, 28));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JTable tabla = new JTable(model);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelGenerico(String titulo, String mensaje) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblTitulo = new JLabel("Módulo de " + titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Roboto", Font.BOLD, 28));
        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblMensaje, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelDeBienvenida() {
        return crearPanelGenerico("Biblioteca Virtual", "Selecciona una opción del menú para consultar los datos.");
    }
}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class VentanaBienvenida extends JFrame {

    public VentanaBienvenida() {
        // --- CONFIGURACIÓN DE LA VENTANA PRINCIPAL (SIN CAMBIOS) ---
        setTitle("Biblioteca Virtual");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        DottedBackgroundPanel backgroundPanel = new DottedBackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(backgroundPanel);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(Color.BLACK, 3, 20),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 120));
        
        JLabel titleLabel = new JLabel("BIBLIOTECA", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(118, 204, 118));
        titleLabel.setBorder(new RoundedBorder(Color.BLACK, 2, 15));
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- PANEL DE BOTONES (CON LÓGICA MODIFICADA) ---
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        buttonPanel.setOpaque(false);

        String[] buttonLabels = {"Productos", "Clientes", "Empleados", "Ventas", "Facturas", "Suministro", "Sucursales", "Proveedores"};
        
        for (String label : buttonLabels) {
            JButton button = createStyledButton(label, Color.WHITE, Color.BLACK);
            
            // --- INICIO DE LA MODIFICACIÓN ---
            // Añadimos un ActionListener para cada botón.
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Usamos un switch para determinar qué ventana abrir según el texto del botón.
                    switch (label) {
                        case "Productos":
                            VentanaProductos ventanaProd = new VentanaProductos();
                            ventanaProd.setLocationRelativeTo(null); // Centrar
                            ventanaProd.setVisible(true); // Mostrar
                            break;
                        case "Clientes":
                            new VentanaClientes().setVisible(true);
                            break;
                        case "Empleados":
                            // Para "Empleados", abrimos la ventana que ya existía.
                            new VentanaEmpleados().setVisible(true);
                            break;
                        case "Ventas":
                            new VentanaVentas().setVisible(true);
                            break;
                        case "Facturas":
                            new VentanaFacturas().setVisible(true);
                            break;
                        case "Suministro":
                            new VentanaSuministro().setVisible(true);
                            break;
                        case "Sucursales":
                            new VentanaSucursales().setVisible(true);
                            break;
                        case "Proveedores":
                            new VentanaProveedores().setVisible(true);
                            break;
                    }
                }
            });
            // --- FIN DE LA MODIFICACIÓN ---
            
            buttonPanel.add(button);
        }
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // --- PIE DE PÁGINA (SIN CAMBIOS) ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel footerLabel = new JLabel("Quito-Matriz");
        footerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        footerPanel.add(footerLabel, BorderLayout.WEST);

        JButton exitButton = createStyledButton("SALIR", new Color(220, 53, 69), Color.WHITE);
        exitButton.addActionListener(e -> System.exit(0));
        
        JPanel exitButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitButtonWrapper.setOpaque(false);
        exitButtonWrapper.add(exitButton);
        footerPanel.add(exitButtonWrapper, BorderLayout.CENTER);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 80));
        button.setBorder(new RoundedBorder(Color.BLACK, 2, 15));
        return button;
    }

    // --- CLASES INTERNAS PARA ESTILOS (SIN CAMBIOS) ---
    class DottedBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(220, 220, 220));
            for (int x = 0; x < getWidth(); x += 20) {
                for (int y = 0; y < getHeight(); y += 20) {
                    g.fillOval(x, y, 2, 2);
                }
            }
        }
    }

    class RoundedBorder implements Border {
        private int radius;
        private Color color;
        private int stroke;

        public RoundedBorder(Color color, int stroke, int radius) {
            this.radius = radius;
            this.color = color;
            this.stroke = stroke;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(this.color);
            g2d.setStroke(new java.awt.BasicStroke(this.stroke));
            g2d.drawRoundRect(x + stroke / 2, y + stroke / 2, width - stroke, height - stroke, radius, radius);
            g2d.dispose();
        }
    }
}

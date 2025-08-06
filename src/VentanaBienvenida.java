import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class VentanaBienvenida extends JFrame {

    public VentanaBienvenida() {
        setTitle("LIBRERIA VIRTUAL");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(new Color(242, 243, 245)); // #f2f3f5
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(backgroundPanel);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setOpaque(false); // Hacer transparente para que se vea el fondo
        // --- CORRECCIÓN: Se eliminó la línea que establecía el borde negro ---
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        // --- Panel del Título ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("LIBRERIA-NEXUS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(237, 125, 49)); // Color naranja
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- Panel de Botones con GridBagLayout para mayor control ---
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        String[] buttonLabels = {"Clientes", "Empleados", "Ventas", "Libros", "Proveedores", "Suministro", "Sucursales", "AUDITORIA", "SALIR"};
        
        int gridx = 0;
        int gridy = 0;

        for (String label : buttonLabels) {
            JButton button;
            if (label.equals("AUDITORIA")) {
                button = createStyledButton(label, new Color(255, 118, 118), Color.BLACK); // Rojo claro
            } else if (label.equals("SALIR")) {
                button = createStyledButton(label, new Color(255, 0, 0), Color.WHITE); // Rojo fuerte
            } else {
                button = createStyledButton(label, new Color(191, 191, 191), Color.BLACK); // Gris
            }
            
            button.addActionListener(new ButtonClickListener());
            
            gbc.gridx = gridx;
            gbc.gridy = gridy;
            buttonPanel.add(button, gbc);

            gridx++;
            if (gridx > 2) {
                gridx = 0;
                gridy++;
            }
        }
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // --- Pie de Página ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));

        JLabel footerLabel = new JLabel("Quito-Matriz");
        footerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        footerPanel.add(footerLabel);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(Color.BLACK, 2, 15));
        button.setPreferredSize(new Dimension(200, 70));
        return button;
    }

    // --- ActionListener centralizado para todos los botones ---
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();
            switch (command) {
                case "Libros":
                    new VentanaLibros().setVisible(true);
                    break;
                case "Clientes":
                    new VentanaClientes().setVisible(true);
                    break;
                case "Empleados":
                    new VentanaEmpleados().setVisible(true);
                    break;
                case "Ventas":
                    new VentanaVentas().setVisible(true);
                    break;
                case "Proveedores":
                    new VentanaProveedores().setVisible(true);
                    break;
                case "Sucursales":
                    new VentanaSucursales().setVisible(true);
                    break;
                case "Suministro":
                    new VentanaSuministro().setVisible(true);
                    break;
                case "AUDITORIA":
                    new VentanaAuditoria().setVisible(true);
                    break;
                case "SALIR":
                    System.exit(0);
                    break;
            }
        }
    }

    // --- Clase interna para el borde redondeado ---
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
            return new Insets(this.stroke, this.stroke, this.stroke, this.stroke);
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

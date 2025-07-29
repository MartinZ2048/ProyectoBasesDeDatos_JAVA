/*
 * =============================================================================
 * --- VentanaBienvenida.java (DISEÑO CORREGIDO Y FINAL) ---
 * Implementa la nueva estética de la GUI, incluyendo una ventana sin bordes
 * que se puede arrastrar, y todos los elementos visuales personalizados.
 * =============================================================================
 */
import java.awt.BasicStroke;
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
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class VentanaBienvenida extends JFrame {
    
    private Point initialClick; // Variable para guardar la posición inicial del clic para arrastrar

    public VentanaBienvenida() {
        // --- CONFIGURACIÓN DE LA VENTANA SIN DECORACIÓN ---
        setUndecorated(true); // Quita la barra de título, minimizar, etc.
        setBackground(new Color(0, 0, 0, 0)); // Fondo transparente para poder ver las esquinas redondeadas
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // El panel principal que dibuja su propio fondo y borde
        MainPanel mainPanel = new MainPanel();
        setContentPane(mainPanel);
        
        // --- FUNCIONALIDAD DE ARRASTRE DE VENTANA ---
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                setLocation(thisX + xMoved, thisY + yMoved);
            }
        });
    }

    /**
     * El panel principal que contiene toda la lógica de dibujo y los componentes de la GUI.
     */
    class MainPanel extends JPanel {

        public MainPanel() {
            setLayout(new BorderLayout());
            setOpaque(false); // Es importante para que el fondo transparente del JFrame funcione
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding general

            // Panel superior para el título de la ventana
            JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topBar.setOpaque(false);
            JLabel windowTitle = new JLabel("Biblioteca Virtual");
            windowTitle.setFont(new Font("Arial", Font.PLAIN, 14));
            add(topBar, BorderLayout.NORTH);

            // Panel de contenido central que contendrá todo lo demás
            JPanel content = new JPanel(new BorderLayout(20, 20));
            content.setOpaque(false);
            add(content, BorderLayout.CENTER);

            // Encabezado "BIBLIOTECA"
            JLabel header = new JLabel("BIBLIOTECA", JLabel.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 60));
            header.setOpaque(true);
            header.setBackground(new Color(118, 204, 118)); // Tono de verde
            header.setBorder(new RoundedBorder(Color.BLACK, 2, 15));
            header.setPreferredSize(new Dimension(0, 100));
            content.add(header, BorderLayout.NORTH);

            // Grid de botones
            JPanel buttonGrid = new JPanel(new GridLayout(2, 4, 15, 15));
            buttonGrid.setOpaque(false);
            String[] buttonLabels = {"Productos", "Clientes", "Empleados", "Ventas", "Facturas", "Suministro", "Sucursales", "Proveedores"};
            for (String label : buttonLabels) {
                JButton button = createStyledButton(label, Color.WHITE, Color.BLACK);
                button.addActionListener(e -> {
                    new VentanaPrincipal().setVisible(true);
                    // Busca la ventana padre (este JFrame) y la cierra
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainPanel.this);
                    topFrame.dispose();
                });
                buttonGrid.add(button);
            }
            content.add(buttonGrid, BorderLayout.CENTER);

            // Panel inferior (Footer)
            JPanel footer = new JPanel(new BorderLayout());
            footer.setOpaque(false);
            footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            
            JLabel footerLabel = new JLabel("Quito-Matriz");
            footerLabel.setFont(new Font("Arial", Font.BOLD, 16));
            footer.add(footerLabel, BorderLayout.WEST);

            JButton exitButton = createStyledButton("SALIR", new Color(220, 53, 69), Color.WHITE);
            exitButton.addActionListener(e -> System.exit(0));
            exitButton.setPreferredSize(new Dimension(180, 60));
            
            // Panel para centrar el botón de salir
            JPanel exitButtonWrapper = new JPanel(); 
            exitButtonWrapper.setOpaque(false);
            exitButtonWrapper.add(exitButton);
            footer.add(exitButtonWrapper, BorderLayout.CENTER);
            
            content.add(footer, BorderLayout.SOUTH);
        }

        private JButton createStyledButton(String text, Color bg, Color fg) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setBackground(bg);
            button.setForeground(fg);
            button.setFocusPainted(false);
            button.setBorder(new RoundedBorder(Color.BLACK, 2, 15));
            return button;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dibuja los puntos de fondo en toda la ventana
            g2d.setColor(new Color(220, 220, 220));
            for (int x = 0; x < getWidth(); x += 20) {
                for (int y = 0; y < getHeight(); y += 20) {
                    g2d.fillOval(x, y, 2, 2);
                }
            }

            // Dibuja el contenedor principal (el que tiene el borde negro)
            g2d.setColor(new Color(240, 240, 240));
            g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 25, 25);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 25, 25);
            
            g2d.dispose();
        }
    }

    /**
     * Una clase de borde para crear esquinas redondeadas en los botones.
     */
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
            return new Insets(this.radius/2, this.radius, this.radius/2, this.radius);
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

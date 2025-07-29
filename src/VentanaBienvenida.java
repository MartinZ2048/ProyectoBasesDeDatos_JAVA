/*
 * =============================================================================
 * --- VentanaBienvenida.java (NUEVO) ---
 * Ventana inicial que da la bienvenida al usuario y ofrece un botón para continuar.
 * Reemplaza la pantalla de login.
 * =============================================================================
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VentanaBienvenida extends JFrame {

    public VentanaBienvenida() {
        // --- CONFIGURACIÓN DE LA VENTANA ---
        setTitle("Bienvenido");
        setSize(800, 600);
        setLocationRelativeTo(null); // Centrar en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // Para centrar el contenido fácilmente

        // --- PANEL PRINCIPAL ---
        JPanel panelCentral = new JPanel(new BorderLayout(10, 20));
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // --- ETIQUETA DE BIENVENIDA (ENCABEZADO) ---
        JLabel lblBienvenida = new JLabel("Bienvenido a la Biblioteca Virtual", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Roboto", Font.BOLD, 24));

        // --- SUBTÍTULO ---
        JLabel lblSubtitulo = new JLabel("Explora nuestro catálogo digital", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Roboto", Font.PLAIN, 16));

        // --- BOTÓN CONTINUAR ---
        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Roboto", Font.PLAIN, 14));
        btnContinuar.setPreferredSize(new Dimension(120, 40));

        // Acción del botón: cerrar esta ventana y abrir la principal
        btnContinuar.addActionListener(e -> {
            new VentanaPrincipal().setVisible(true);
            this.dispose(); // Cierra la ventana de bienvenida
        });
        
        // Panel para el botón para poder centrarlo
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnContinuar);

        // --- AÑADIR COMPONENTES AL PANEL CENTRAL ---
        panelCentral.add(lblBienvenida, BorderLayout.NORTH);
        panelCentral.add(lblSubtitulo, BorderLayout.CENTER);
        panelCentral.add(panelBoton, BorderLayout.SOUTH);

        // --- AÑADIR PANEL CENTRAL A LA VENTANA ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelCentral, gbc);
    }
}

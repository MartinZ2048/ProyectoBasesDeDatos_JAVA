import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Panel que contiene el formulario de inicio de sesión con sus componentes.
 * Hereda de JPanel para poder ser añadido a cualquier ventana.
 */
public class PanelLogin extends JPanel {

    public PanelLogin() {
        // --- CONFIGURACIÓN DEL PANEL ---
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout()); // Usamos GridBagLayout para centrar el formulario

        // --- CREACIÓN DEL FORMULARIO INTERNO ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Etiqueta y campo de texto para el Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Usuario"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField txtUsuario = new JTextField(25);
        panelFormulario.add(txtUsuario, gbc);

        // Etiqueta y campo de texto para la Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panelFormulario.add(new JLabel("Contraseña"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField txtPassword = new JPasswordField(25);
        panelFormulario.add(txtPassword, gbc);

        // Botón de Conectar
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnConectar = new JButton("Conectar");
        panelFormulario.add(btnConectar, gbc);

        // Añadimos el panel del formulario al panel principal para centrarlo
        add(panelFormulario);
    }
}

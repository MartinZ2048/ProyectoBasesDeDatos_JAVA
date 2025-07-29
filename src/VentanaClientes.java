import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

// La clase AHORA extiende JFrame para ser una ventana
public class VentanaClientes extends JFrame {

    public VentanaClientes() {
        setTitle("Clientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel label = new JLabel("Gestión de Clientes", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        contentPane.add(label, BorderLayout.CENTER);
    }
}

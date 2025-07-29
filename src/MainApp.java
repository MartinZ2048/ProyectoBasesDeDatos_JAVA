/*
 * =============================================================================
 * --- MainApp.java ---
 * Punto de entrada de la aplicación.
 * Inicia la nueva ventana de bienvenida.
 * =============================================================================
 */
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainApp {

    public static void main(String[] args) {
        // Configura el Look and Feel de FlatLaf para un aspecto moderno.
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize LaF: " + e.getMessage());
        }

        // Crea y muestra la ventana de bienvenida en el hilo de despacho de eventos de Swing.
        SwingUtilities.invokeLater(() -> {
            new VentanaBienvenida().setVisible(true);
        });
    }
}

import ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Apply system look-and-feel for native font rendering on Windows
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

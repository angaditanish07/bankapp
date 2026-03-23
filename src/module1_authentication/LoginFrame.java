package module1_authentication;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField vpaField = new JTextField(40);
    private final JPasswordField pinField = new JPasswordField(40);

    public LoginFrame() {
        super("UPI Minimal - Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(220, 237, 250));
        main.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Secure UPI Portal");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        main.add(title, c);

        c.gridy++;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        main.add(new JLabel("VPA"), c);

        c.gridx = 1;
        main.add(vpaField, c);

        c.gridx = 0;
        c.gridy++;
        main.add(new JLabel("PIN"), c);

        c.gridx = 1;
        main.add(pinField, c);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(135, 206, 235));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loginBtn.setPreferredSize(new Dimension(200, 50));

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        main.add(loginBtn, c);

        JButton registerBtn = new JButton("Create account");
        registerBtn.setFocusPainted(false);
        registerBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        registerBtn.setPreferredSize(new Dimension(200, 50));

        c.gridy++;
        main.add(registerBtn, c);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        setContentPane(main);
        setVisible(true);
    }

    private void login() {
        String vpa = vpaField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        if (vpa.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill both VPA and PIN", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AuthenticationService auth = new AuthenticationService();
        User user = auth.login(vpa, pin);

        if (user != null) {
            dispose();
            new DashboardFrame(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

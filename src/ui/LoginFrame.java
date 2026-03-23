package ui;

import model.User;
import service.AuthenticationService;
import util.UITheme;
import util.Validator;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField     vpaField = UITheme.textField();
    private final JPasswordField pinField = UITheme.passwordField();
    private final JLabel         vpaErr   = UITheme.errorLabel();
    private final JLabel         pinErr   = UITheme.errorLabel();

    public LoginFrame() {
        super("UPI Pay — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);

        // Dark gradient background
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(26, 26, 46),
                    getWidth(), getHeight(), new Color(30, 60, 114)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(false);

        // White card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createEmptyBorder(44, 48, 44, 48));
        card.setPreferredSize(new Dimension(420, 500));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 0;
        int row = 0;

        // Logo + title (centered)
        JLabel logo = new JLabel("\uD83D\uDCB3", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 44));
        c.gridy = row++; c.insets = new Insets(0, 0, 4, 0);
        card.add(logo, c);

        JLabel title = new JLabel("UPI Pay", SwingConstants.CENTER);
        title.setFont(UITheme.F_TITLE);
        title.setForeground(UITheme.ACCENT_BLUE);
        c.gridy = row++; c.insets = new Insets(0, 0, 4, 0);
        card.add(title, c);

        JLabel sub = new JLabel("Sign in to your account", SwingConstants.CENTER);
        sub.setFont(UITheme.F_BODY);
        sub.setForeground(UITheme.TEXT_MUTED);
        c.gridy = row++; c.insets = new Insets(0, 0, 28, 0);
        card.add(sub, c);

        // VPA field
        c.gridy = row++; c.insets = new Insets(0, 0, 4, 0);
        card.add(fieldLabel("VPA Address (e.g. name@upi)"), c);

        vpaField.setPreferredSize(new Dimension(0, 42));
        c.gridy = row++; c.insets = new Insets(0, 0, 0, 0);
        card.add(vpaField, c);

        c.gridy = row++; c.insets = new Insets(0, 0, 8, 0);
        card.add(vpaErr, c);

        // PIN field
        c.gridy = row++; c.insets = new Insets(0, 0, 4, 0);
        card.add(fieldLabel("UPI PIN"), c);

        pinField.setPreferredSize(new Dimension(0, 42));
        c.gridy = row++; c.insets = new Insets(0, 0, 0, 0);
        card.add(pinField, c);

        c.gridy = row++; c.insets = new Insets(0, 0, 20, 0);
        card.add(pinErr, c);

        // Buttons
        JButton loginBtn = UITheme.primaryButton("Login");
        loginBtn.setPreferredSize(new Dimension(0, 44));
        c.gridy = row++; c.insets = new Insets(0, 0, 8, 0);
        card.add(loginBtn, c);

        JButton registerBtn = UITheme.secondaryButton("Create Account");
        registerBtn.setPreferredSize(new Dimension(0, 44));
        c.gridy = row++; c.insets = new Insets(0, 0, 0, 0);
        card.add(registerBtn, c);

        loginBtn.addActionListener(e -> doLogin());
        registerBtn.addActionListener(e -> { dispose(); new RegisterFrame(); });

        root.add(card);
        setContentPane(root);
        setVisible(true);
    }

    private void doLogin() {
        String vpa = vpaField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();
        boolean ok = true;

        if (!Validator.isValidVPA(vpa)) { vpaErr.setText(Validator.vpaError(vpa)); ok = false; }
        else vpaErr.setText(" ");

        if (!Validator.isValidPin(pin)) { pinErr.setText(Validator.pinError(pin)); ok = false; }
        else pinErr.setText(" ");

        if (!ok) return;

        User user = new AuthenticationService().login(vpa, pin);
        if (user != null) {
            dispose();
            new DashboardFrame(user);
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid VPA or PIN. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.F_LABEL);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }
}

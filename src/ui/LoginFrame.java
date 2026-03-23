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

        // ── Root: dark gradient backdrop ──────────────────────────────
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

        // ── Card panel ───────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createEmptyBorder(40, 44, 40, 44));
        card.setPreferredSize(new Dimension(420, 520));

        // Logo area
        JLabel logo = new JLabel("💳", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        logo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel appTitle = new JLabel("UPI Pay");
        appTitle.setFont(UITheme.F_TITLE);
        appTitle.setForeground(UITheme.ACCENT_BLUE);
        appTitle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to your account");
        subtitle.setFont(UITheme.F_BODY);
        subtitle.setForeground(UITheme.TEXT_MUTED);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        JButton loginBtn    = UITheme.primaryButton("Login");
        JButton registerBtn = UITheme.secondaryButton("Create Account");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        vpaField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        pinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        card.add(logo);
        card.add(Box.createVerticalStrut(6));
        card.add(appTitle);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));
        card.add(fieldLabel("VPA Address"));
        card.add(Box.createVerticalStrut(4));
        card.add(vpaField);
        card.add(vpaErr);
        card.add(Box.createVerticalStrut(12));
        card.add(fieldLabel("UPI PIN"));
        card.add(Box.createVerticalStrut(4));
        card.add(pinField);
        card.add(pinErr);
        card.add(Box.createVerticalStrut(24));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(registerBtn);

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
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }
}

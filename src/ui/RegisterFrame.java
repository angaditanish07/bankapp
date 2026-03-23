package ui;

import model.User;
import service.AuthenticationService;
import util.UITheme;
import util.Validator;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final JTextField     nameField   = UITheme.textField();
    private final JTextField     mobileField = UITheme.textField();
    private final JTextField     emailField  = UITheme.textField();
    private final JTextField     vpaField    = UITheme.textField();
    private final JPasswordField pinField    = UITheme.passwordField();
    private final JPasswordField pin2Field   = UITheme.passwordField();

    private final JLabel nameErr   = UITheme.errorLabel();
    private final JLabel mobileErr = UITheme.errorLabel();
    private final JLabel emailErr  = UITheme.errorLabel();
    private final JLabel vpaErr    = UITheme.errorLabel();
    private final JLabel pinErr    = UITheme.errorLabel();
    private final JLabel pin2Err   = UITheme.errorLabel();

    public RegisterFrame() {
        super("UPI Pay — Create Account");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(26, 26, 46),
                    getWidth(), getHeight(), new Color(30, 60, 114)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createEmptyBorder(36, 44, 36, 44));
        card.setPreferredSize(new Dimension(440, 620));

        JLabel title = new JLabel("Create your UPI account");
        title.setFont(UITheme.F_HEADING);
        title.setForeground(UITheme.TEXT_DARK);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JButton registerBtn = UITheme.primaryButton("Create Account");
        JButton backBtn     = UITheme.secondaryButton("Back to Login");
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        for (JTextField f : new JTextField[]{nameField, mobileField, emailField, vpaField}) {
            f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        }
        pinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        pin2Field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        card.add(title);
        card.add(Box.createVerticalStrut(24));
        card.add(row("Full Name",    nameField,   nameErr));
        card.add(row("Mobile",       mobileField, mobileErr));
        card.add(row("Email",        emailField,  emailErr));
        card.add(row("VPA",          vpaField,    vpaErr));
        card.add(row("UPI PIN",      pinField,    pinErr));
        card.add(row("Confirm PIN",  pin2Field,   pin2Err));
        card.add(Box.createVerticalStrut(20));
        card.add(registerBtn);
        card.add(Box.createVerticalStrut(8));
        card.add(backBtn);

        registerBtn.addActionListener(e -> doRegister());
        backBtn.addActionListener(e -> { dispose(); new LoginFrame(); });

        root.add(card);
        setContentPane(root);
        setVisible(true);
    }

    private JPanel row(String label, JComponent field, JLabel err) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.F_LABEL);
        lbl.setForeground(UITheme.TEXT_MUTED);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        field.setAlignmentX(LEFT_ALIGNMENT);
        err.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(3));
        p.add(field);
        p.add(err);
        p.add(Box.createVerticalStrut(8));
        return p;
    }

    private void doRegister() {
        String name   = nameField.getText().trim();
        String mobile = mobileField.getText().trim();
        String email  = emailField.getText().trim();
        String vpa    = vpaField.getText().trim();
        String pin    = new String(pinField.getPassword()).trim();
        String pin2   = new String(pin2Field.getPassword()).trim();
        boolean ok    = true;

        if (name.isEmpty())              { nameErr.setText("Name is required"); ok = false; } else nameErr.setText(" ");
        String mErr = Validator.mobileError(mobile); if (mErr != null) { mobileErr.setText(mErr); ok = false; } else mobileErr.setText(" ");
        String eErr = Validator.emailError(email);   if (eErr != null) { emailErr.setText(eErr);  ok = false; } else emailErr.setText(" ");
        String vErr = Validator.vpaError(vpa);       if (vErr != null) { vpaErr.setText(vErr);    ok = false; } else vpaErr.setText(" ");
        String pErr = Validator.pinError(pin);       if (pErr != null) { pinErr.setText(pErr);    ok = false; } else pinErr.setText(" ");
        if (!pin.equals(pin2))           { pin2Err.setText("PINs do not match"); ok = false; }     else pin2Err.setText(" ");

        if (!ok) return;

        AuthenticationService auth = new AuthenticationService();
        if (auth.vpaExists(vpa)) {
            vpaErr.setText("This VPA is already taken");
            return;
        }

        User user = new User(0, name, mobile, email, vpa, pin, 0.0);
        if (auth.register(user)) {
            JOptionPane.showMessageDialog(this, "Account created! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

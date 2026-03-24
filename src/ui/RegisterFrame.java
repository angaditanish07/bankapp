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
        super("Pay Flow— Create Account");
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
        root.setOpaque(false);

        // Scrollable card for smaller screens
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createEmptyBorder(36, 48, 36, 48));
        card.setPreferredSize(new Dimension(440, 660));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 0;
        int row = 0;

        // Title
        JLabel title = new JLabel("Create your UPI account");
        title.setFont(UITheme.F_HEADING);
        title.setForeground(UITheme.TEXT_DARK);
        c.gridy = row++; c.insets = new Insets(0, 0, 24, 0);
        card.add(title, c);

        // Form fields
        row = addField(card, c, row, "Full Name",      nameField,   nameErr);
        row = addField(card, c, row, "Mobile Number",  mobileField, mobileErr);
        row = addField(card, c, row, "Email Address",  emailField,  emailErr);
        row = addField(card, c, row, "VPA (e.g. name@upi)", vpaField, vpaErr);
        row = addField(card, c, row, "UPI PIN (4–6 digits)", pinField, pinErr);
        row = addField(card, c, row, "Confirm PIN",    pin2Field,   pin2Err);

        // Buttons
        JButton registerBtn = UITheme.primaryButton("Create Account");
        registerBtn.setPreferredSize(new Dimension(0, 44));
        c.gridy = row++; c.insets = new Insets(8, 0, 8, 0);
        card.add(registerBtn, c);

        JButton backBtn = UITheme.secondaryButton("Back to Login");
        backBtn.setPreferredSize(new Dimension(0, 44));
        c.gridy = row++; c.insets = new Insets(0, 0, 0, 0);
        card.add(backBtn, c);

        registerBtn.addActionListener(e -> doRegister());
        backBtn.addActionListener(e -> { dispose(); new LoginFrame(); });

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        root.add(card);
        setContentPane(root);
        setVisible(true);
    }

    /** Adds label + field + error label in one GBL row group, returns next row index */
    private int addField(JPanel card, GridBagConstraints c, int row,
                         String label, JComponent field, JLabel err) {
        c.gridy = row++; c.insets = new Insets(0, 0, 4, 0);
        card.add(fieldLabel(label), c);

        field.setPreferredSize(new Dimension(0, 42));
        c.gridy = row++; c.insets = new Insets(0, 0, 0, 0);
        card.add(field, c);

        c.gridy = row++; c.insets = new Insets(0, 0, 6, 0);
        card.add(err, c);

        return row;
    }

    private void doRegister() {
        String name   = nameField.getText().trim();
        String mobile = mobileField.getText().trim();
        String email  = emailField.getText().trim();
        String vpa    = vpaField.getText().trim();
        String pin    = new String(pinField.getPassword()).trim();
        String pin2   = new String(pin2Field.getPassword()).trim();
        boolean ok    = true;

        if (name.isEmpty()) { nameErr.setText("Name is required"); ok = false; } else nameErr.setText(" ");
        String mErr = Validator.mobileError(mobile); if (mErr != null) { mobileErr.setText(mErr); ok = false; } else mobileErr.setText(" ");
        String eErr = Validator.emailError(email);   if (eErr != null) { emailErr.setText(eErr);  ok = false; } else emailErr.setText(" ");
        String vErr = Validator.vpaError(vpa);       if (vErr != null) { vpaErr.setText(vErr);    ok = false; } else vpaErr.setText(" ");
        String pErr = Validator.pinError(pin);       if (pErr != null) { pinErr.setText(pErr);    ok = false; } else pinErr.setText(" ");
        if (!pin.equals(pin2)) { pin2Err.setText("PINs do not match"); ok = false; } else pin2Err.setText(" ");
        if (!ok) return;

        AuthenticationService auth = new AuthenticationService();
        if (auth.vpaExists(vpa)) { vpaErr.setText("This VPA is already taken"); return; }

        User user = new User(0, name, mobile, email, vpa, pin, 0.0);
        if (auth.register(user)) {
            JOptionPane.showMessageDialog(this, "Account created! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); new LoginFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.F_LABEL);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }
}

package module1_authentication;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final JTextField nameField = new JTextField(40);
    private final JTextField mobileField = new JTextField(40);
    private final JTextField emailField = new JTextField(40);
    private final JTextField vpaField = new JTextField(40);

    public RegisterFrame() {
        super("UPI Minimal - Register");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(220, 237, 250));
        main.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        JLabel title = new JLabel("Create your UPI account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        main.add(title, c);

        c.gridwidth = 1;
        c.gridy++;
        main.add(new JLabel("Full name"), c);
        c.gridx = 1;
        main.add(nameField, c);

        c.gridx = 0;
        c.gridy++;
        main.add(new JLabel("Mobile"), c);
        c.gridx = 1;
        main.add(mobileField, c);

        c.gridx = 0;
        c.gridy++;
        main.add(new JLabel("Email"), c);
        c.gridx = 1;
        main.add(emailField, c);

        c.gridx = 0;
        c.gridy++;
        main.add(new JLabel("VPA"), c);
        c.gridx = 1;
        main.add(vpaField, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(135, 206, 235));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        registerBtn.setPreferredSize(new Dimension(200, 50));
        main.add(registerBtn, c);

        c.gridy++;
        JButton backBtn = new JButton("Back to Login");
        backBtn.setFocusPainted(false);
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        backBtn.setPreferredSize(new Dimension(200, 50));
        main.add(backBtn, c);

        registerBtn.addActionListener(e -> registerUser());
        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setContentPane(main);
        setVisible(true);
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String mobile = mobileField.getText().trim();
        String email = emailField.getText().trim();
        String vpa = vpaField.getText().trim();

        if (name.isEmpty() || mobile.isEmpty() || email.isEmpty() || vpa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = new User(0, name, mobile, email, vpa, "1234");
        UserDAO dao = new UserDAO();

        if (dao.registerUser(user)) {
            JOptionPane.showMessageDialog(this, "Account registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Try a different VPA.", "Failure", JOptionPane.ERROR_MESSAGE);
        }
    }
}

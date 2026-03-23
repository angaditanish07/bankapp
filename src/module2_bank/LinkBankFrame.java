package module2_bank;

import module1_authentication.User;
import module1_authentication.DashboardFrame;

import javax.swing.*;
import java.awt.*;

public class LinkBankFrame extends JFrame {

    private final JTextField accField = new JTextField(40);
    private final JTextField bankField = new JTextField(40);
    private final JTextField balanceField = new JTextField(40);
    private final JTextField emailField = new JTextField(40);
    private final User currentUser;

    public LinkBankFrame(User user) {
        super("Link Bank Account");

        currentUser = user;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(220, 237, 250));
        main.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        JLabel heading = new JLabel("Link a Bank Account");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        main.add(heading, c);

        c.gridy++;
        main.add(new JLabel("Account Number"), c);
        c.gridx = 1;
        main.add(accField, c);

        c.gridx = 0;
        c.gridy++;
        main.add(new JLabel("Bank Name"), c);
        c.gridx = 1;
        main.add(bankField, c);

        c.gridx = 0;
        c.gridy++;
        main.add(new JLabel("Initial Balance"), c);
        c.gridx = 1;
        main.add(balanceField, c);

        c.gridx = 0;
        c.gridy++;
        main.add(new JLabel("User Email"), c);
        c.gridx = 1;
        main.add(emailField, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton linkButton = new JButton("Link Account");
        linkButton.setBackground(new Color(135, 206, 235));
        linkButton.setForeground(Color.WHITE);
        linkButton.setFocusPainted(false);
        linkButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        linkButton.setPreferredSize(new Dimension(200, 50));
        main.add(linkButton, c);

        c.gridy++;
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(200, 50));
        main.add(backButton, c);

        linkButton.addActionListener(e -> linkAccount());
        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame(currentUser);
        });

        setContentPane(main);
        setVisible(true);
    }

    private void linkAccount() {
        String acc = accField.getText().trim();
        String bank = bankField.getText().trim();
        String balanceText = balanceField.getText().trim();
        String email = emailField.getText().trim();

        if (acc.isEmpty() || bank.isEmpty() || balanceText.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double balance = Double.parseDouble(balanceText);

            BankAccount account = new BankAccount(acc, bank, balance, email);
            boolean status = AccountDAO.linkBankAccount(account);

            if (status) {
                JOptionPane.showMessageDialog(this, "Bank account linked successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new DashboardFrame(currentUser);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to link bank account", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Enter a numeric balance value", "Validation", JOptionPane.WARNING_MESSAGE);
        }
    }
}
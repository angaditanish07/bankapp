package module1_authentication;

import module2_bank.LinkBankFrame;
import module3_payments.SendMoneyFrame;
import module4_history.TransactionHistoryFrame;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private final User currentUser;

    public DashboardFrame(User user) {
        super("UPI Minimal - Dashboard");

        currentUser = user;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(220, 237, 250));
        main.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        JLabel header = new JLabel("Hello, " + currentUser.getName());
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        main.add(header, c);

        c.gridy++;
        JLabel sub = new JLabel("VPA: " + currentUser.getVpa());
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        main.add(sub, c);

        c.gridy++;
        JLabel balance = new JLabel("Balance: ₹" + String.format("%.2f", currentUser.getBalance()));
        balance.setFont(new Font("Segoe UI", Font.BOLD, 16));
        balance.setForeground(new Color(0, 100, 0));
        main.add(balance, c);

        c.gridy++;
        c.gridwidth = 1;
        c.gridx = 0;
        JButton linkBank = createButton("Link Bank");
        main.add(linkBank, c);

        c.gridx = 1;
        JButton sendMoney = createButton("Send Money");
        main.add(sendMoney, c);

        c.gridx = 0;
        c.gridy++;
        JButton history = createButton("Transaction History");
        main.add(history, c);

        c.gridx = 1;
        JButton logout = createButton("Logout");
        logout.setBackground(new Color(255, 182, 193));
        logout.setForeground(Color.BLACK);
        main.add(logout, c);

        linkBank.addActionListener(e -> {
            dispose();
            new LinkBankFrame(currentUser);
        });

        sendMoney.addActionListener(e -> {
            dispose();
            new SendMoneyFrame(currentUser);
        });

        history.addActionListener(e -> {
            dispose();
            new TransactionHistoryFrame(currentUser);
        });

        logout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setContentPane(main);
        setVisible(true);
    }

    private JButton createButton(String name) {
        JButton button = new JButton(name);
        button.setFocusPainted(false);
        button.setBackground(new Color(135, 206, 235));
        button.setForeground(Color.BLUE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }
}

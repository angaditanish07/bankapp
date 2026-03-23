package module3_payments;

import module1_authentication.User;
import module1_authentication.DashboardFrame;
import module4_history.TransactionHistoryFrame;

import javax.swing.*;
import java.awt.*;

public class SendMoneyFrame extends JFrame {

    private final JTextField receiverField = new JTextField(40);
    private final JTextField amountField = new JTextField(40);
    private final User sender;

    public SendMoneyFrame(User user) {
        super("Send Money");

        sender = user;

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
        c.gridwidth = 2;
        JLabel title = new JLabel("Transfer Funds");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        main.add(title, c);

        c.gridy++;
        c.gridwidth = 1;
        main.add(new JLabel("Receiver VPA"), c);
        c.gridx = 1;
        main.add(receiverField, c);

        c.gridx = 0;
        c.gridy++;
        main.add(new JLabel("Amount"), c);
        c.gridx = 1;
        main.add(amountField, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton sendBtn = new JButton("Send");
        sendBtn.setBackground(new Color(135, 206, 235));
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFocusPainted(false);
        sendBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sendBtn.setPreferredSize(new Dimension(200, 50));
        main.add(sendBtn, c);

        c.gridy++;
        JButton historyBtn = new JButton("View History");
        historyBtn.setFocusPainted(false);
        historyBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        historyBtn.setPreferredSize(new Dimension(200, 50));
        main.add(historyBtn, c);

        c.gridy++;
        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.setFocusPainted(false);
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        backBtn.setPreferredSize(new Dimension(200, 50));
        main.add(backBtn, c);

        sendBtn.addActionListener(e -> sendMoney());
        historyBtn.addActionListener(e -> {
            dispose();
            new TransactionHistoryFrame(sender);
        });
        backBtn.addActionListener(e -> {
            dispose();
            new DashboardFrame(sender);
        });

        setContentPane(main);
        setVisible(true);
    }

    private void sendMoney() {
        String receiver = receiverField.getText().trim();
        String amountString = amountField.getText().trim();

        if (receiver.isEmpty() || amountString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Receiver and amount are required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountString);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PaymentService service = new PaymentService();
        boolean success = service.transfer(sender.getVpa(), receiver, amount);

        if (success) {
            JOptionPane.showMessageDialog(this, "Payment completed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new DashboardFrame(sender);
        } else {
            JOptionPane.showMessageDialog(this, "Payment failed. Check VPA and balance", "Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

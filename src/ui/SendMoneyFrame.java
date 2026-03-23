package ui;

import model.User;
import service.PaymentService;
import ui.components.SidebarPanel;
import util.UITheme;
import util.Validator;
import javax.swing.*;
import java.awt.*;

public class SendMoneyFrame extends JFrame {

    private final JTextField     receiverField = UITheme.textField();
    private final JTextField     amountField   = UITheme.textField();
    private final JTextField     noteField     = UITheme.textField();
    private final JLabel         receiverErr   = UITheme.errorLabel();
    private final JLabel         amountErr     = UITheme.errorLabel();

    private final User           sender;
    private final PaymentService payService = new PaymentService();

    public SendMoneyFrame(User user) {
        super("UPI Pay — Send Money");
        this.sender = user;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_APP);
        root.add(new SidebarPanel(user, "sendmoney", this), BorderLayout.WEST);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(UITheme.BG_APP);
        content.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(480, 460));

        JLabel header = new JLabel("Transfer Funds");
        header.setFont(UITheme.F_HEADING);
        header.setForeground(UITheme.TEXT_DARK);
        header.setAlignmentX(LEFT_ALIGNMENT);

        JLabel balLbl = new JLabel("Available balance: ₹" + String.format("%,.2f", sender.getBalance()));
        balLbl.setFont(UITheme.F_LABEL);
        balLbl.setForeground(UITheme.ACCENT_GREEN);
        balLbl.setAlignmentX(LEFT_ALIGNMENT);

        for (JTextField f : new JTextField[]{receiverField, amountField, noteField})
            f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton sendBtn = UITheme.primaryButton("Send Money");
        sendBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        card.add(header);
        card.add(Box.createVerticalStrut(4));
        card.add(balLbl);
        card.add(Box.createVerticalStrut(24));
        card.add(fieldRow("Receiver VPA",    receiverField, receiverErr));
        card.add(fieldRow("Amount (₹)",      amountField,   amountErr));
        card.add(fieldRow("Note (Optional)", noteField,     new JLabel(" ")));
        card.add(Box.createVerticalStrut(16));
        card.add(sendBtn);

        sendBtn.addActionListener(e -> doSend());
        content.add(card);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }

    private void doSend() {
        String rcvVpa = receiverField.getText().trim();
        String amtStr = amountField.getText().trim();
        String note   = noteField.getText().trim();
        boolean ok    = true;

        if (!Validator.isValidVPA(rcvVpa)) { receiverErr.setText(Validator.vpaError(rcvVpa)); ok = false; } else receiverErr.setText(" ");
        if (!Validator.isValidAmount(amtStr)) { amountErr.setText(Validator.amountError(amtStr)); ok = false; } else amountErr.setText(" ");
        if (!ok) return;

        double amount = Double.parseDouble(amtStr);

        // ── PIN confirmation dialog ───────────────────────────────────
        JPasswordField pinInput = UITheme.passwordField();
        pinInput.setPreferredSize(new Dimension(260, 42));
        Object[] prompt = {
            new JLabel("Enter your UPI PIN to confirm ₹" + String.format("%,.2f", amount) + " → " + rcvVpa),
            Box.createVerticalStrut(6),
            pinInput
        };
        int choice = JOptionPane.showConfirmDialog(this, prompt,
            "Confirm Transaction", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) return;

        String pin = new String(pinInput.getPassword()).trim();

        PaymentService.Result result = payService.transfer(sender, rcvVpa, amount, pin, note);

        if (result == PaymentService.Result.SUCCESS) {
            JOptionPane.showMessageDialog(this,
                "✅ Payment of ₹" + String.format("%,.2f", amount) + " sent to " + rcvVpa + " successfully!",
                "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new DashboardFrame(sender);
        } else {
            JOptionPane.showMessageDialog(this, result.message(), "Transaction Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel fieldRow(String label, JComponent field, JLabel err) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false); p.setAlignmentX(LEFT_ALIGNMENT);
        JLabel l = new JLabel(label); l.setFont(UITheme.F_LABEL); l.setForeground(UITheme.TEXT_MUTED);
        l.setAlignmentX(LEFT_ALIGNMENT); field.setAlignmentX(LEFT_ALIGNMENT); err.setAlignmentX(LEFT_ALIGNMENT);
        p.add(l); p.add(Box.createVerticalStrut(3)); p.add(field); p.add(err); p.add(Box.createVerticalStrut(10));
        return p;
    }
}

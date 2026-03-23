import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendMoneyFrame extends JFrame {

    JTextField senderField;
    JTextField receiverField;
    JTextField amountField;

    JButton sendButton;

    public SendMoneyFrame() {

        setTitle("UPI Send Money");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel senderLabel = new JLabel("Sender Email:");
        senderLabel.setBounds(20, 30, 120, 25);
        add(senderLabel);

        senderField = new JTextField();
        senderField.setBounds(150, 30, 150, 25);
        add(senderField);

        JLabel receiverLabel = new JLabel("Receiver Email:");
        receiverLabel.setBounds(20, 70, 120, 25);
        add(receiverLabel);

        receiverField = new JTextField();
        receiverField.setBounds(150, 70, 150, 25);
        add(receiverField);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(20, 110, 120, 25);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(150, 110, 150, 25);
        add(amountField);

        sendButton = new JButton("Send Money");
        sendButton.setBounds(110, 160, 120, 30);
        add(sendButton);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String sender = senderField.getText();
                String receiver = receiverField.getText();
                double amount = Double.parseDouble(amountField.getText());

                boolean status = PaymentService.sendMoney(sender, receiver, amount);

                if (status) {
                    JOptionPane.showMessageDialog(null, "Payment Successful");
                } else {
                    JOptionPane.showMessageDialog(null, "Payment Failed");
                }
            }
        });

        setVisible(true);
    }
}
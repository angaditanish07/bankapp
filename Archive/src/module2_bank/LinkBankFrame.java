import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LinkBankFrame extends JFrame {

    JTextField accField;
    JTextField bankField;
    JTextField balanceField;
    JTextField emailField;

    JButton linkButton;

    public LinkBankFrame() {

        setTitle("Link Bank Account");
        setSize(350, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel accLabel = new JLabel("Account Number:");
        accLabel.setBounds(20, 30, 120, 25);
        add(accLabel);

        accField = new JTextField();
        accField.setBounds(150, 30, 150, 25);
        add(accField);

        JLabel bankLabel = new JLabel("Bank Name:");
        bankLabel.setBounds(20, 70, 120, 25);
        add(bankLabel);

        bankField = new JTextField();
        bankField.setBounds(150, 70, 150, 25);
        add(bankField);

        JLabel balanceLabel = new JLabel("Initial Balance:");
        balanceLabel.setBounds(20, 110, 120, 25);
        add(balanceLabel);

        balanceField = new JTextField();
        balanceField.setBounds(150, 110, 150, 25);
        add(balanceField);

        JLabel emailLabel = new JLabel("User Email:");
        emailLabel.setBounds(20, 150, 120, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 150, 150, 25);
        add(emailField);

        linkButton = new JButton("Link Account");
        linkButton.setBounds(100, 200, 120, 30);
        add(linkButton);

        linkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String acc = accField.getText();
                String bank = bankField.getText();
                double balance = Double.parseDouble(balanceField.getText());
                String email = emailField.getText();

                BankAccount account = new BankAccount(acc, bank, balance, email);

                boolean status = AccountDAO.linkBankAccount(account);

                if (status) {
                    JOptionPane.showMessageDialog(null, "Bank Account Linked Successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Error Linking Account");
                }
            }
        });

        setVisible(true);
    }
}
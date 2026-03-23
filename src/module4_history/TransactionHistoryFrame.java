package module4_history;

import module1_authentication.User;
import module1_authentication.DashboardFrame;
import module3_payments.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class TransactionHistoryFrame extends JFrame {

    public TransactionHistoryFrame(User user) {
        super("Transaction History - " + user.getVpa());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        main.setBackground(new Color(220, 237, 250));

        // Header with user info and balance
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridBagLayout());
        headerPanel.setBackground(new Color(220, 237, 250));
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 10, 5, 10);
        
        JLabel title = new JLabel("Transaction History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        gc.gridx = 0;
        gc.gridy = 0;
        headerPanel.add(title, gc);
        
        JLabel userInfo = new JLabel("User: " + user.getName() + " | VPA: " + user.getVpa());
        userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gc.gridy = 1;
        headerPanel.add(userInfo, gc);

        main.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Sender VPA", "Receiver VPA", "Amount", "Status", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ArrayList<Transaction> list = TransactionDAO.getTransactions(user.getVpa());

        if (list.size() == 0) {
            JLabel noData = new JLabel("No transactions found");
            noData.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            noData.setHorizontalAlignment(SwingConstants.CENTER);
            main.add(noData, BorderLayout.CENTER);
        } else {
            for (Transaction t : list) {
                Object[] row = {
                    t.getSenderEmail(),
                    t.getReceiverEmail(),
                    "₹" + String.format("%.2f", t.getAmount()),
                    t.getStatus(),
                    t.getDate() != null ? t.getDate().toString() : "N/A"
                };
                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.setBackground(new Color(135, 206, 235));
            header.setForeground(Color.WHITE);
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(135, 206, 235), 2));
            main.add(scrollPane, BorderLayout.CENTER);
        }

        // Bottom panel with button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        
        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.setBackground(new Color(135, 206, 235));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        backBtn.setPreferredSize(new Dimension(200, 50));
        backBtn.addActionListener(e -> {
            dispose();
            new DashboardFrame(user);
        });

        bottomPanel.add(backBtn);
        main.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(main);
        setVisible(true);
    }
}
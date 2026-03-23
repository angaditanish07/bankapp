import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class TransactionHistoryFrame extends JFrame {

    JTable table;

    public TransactionHistoryFrame(String email) {

        setTitle("Transaction History");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columns = {"Sender", "Receiver", "Amount", "Status"};

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        ArrayList<Transaction> list = TransactionDAO.getTransactions(email);

        for (Transaction t : list) {

            Object[] row = {
                    t.getSenderEmail(),
                    t.getReceiverEmail(),
                    t.getAmount(),
                    t.getStatus()
            };

            model.addRow(row);
        }

        table = new JTable(model);

        JScrollPane pane = new JScrollPane(table);

        add(pane);

        setVisible(true);
    }
}
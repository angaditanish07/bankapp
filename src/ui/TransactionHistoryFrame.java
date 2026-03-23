package ui;

import dao.TransactionDAO;
import model.Transaction;
import model.User;
import ui.components.SidebarPanel;
import util.UITheme;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class TransactionHistoryFrame extends JFrame {

    private final User   user;
    private DefaultTableModel tableModel;

    public TransactionHistoryFrame(User user) {
        super("UPI Pay — Transaction History");
        this.user = user;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_APP);
        root.add(new SidebarPanel(user, "history", this), BorderLayout.WEST);

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(UITheme.BG_APP);
        content.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        // ── Header row with search & filter ──────────────────────────
        JPanel topBar = new JPanel(new BorderLayout(12, 0));
        topBar.setOpaque(false);

        JLabel header = new JLabel("Transaction History");
        header.setFont(UITheme.F_TITLE);
        header.setForeground(UITheme.TEXT_DARK);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        JTextField searchField = UITheme.textField();
        searchField.setPreferredSize(new Dimension(220, 38));
        searchField.putClientProperty("placeholder", "Search VPA or note...");

        String[] statuses = {"ALL", "SUCCESS", "FAILED", "PENDING"};
        JComboBox<String> statusBox = new JComboBox<>(statuses);
        statusBox.setFont(UITheme.F_BODY);
        statusBox.setPreferredSize(new Dimension(130, 38));

        JButton searchBtn = UITheme.primaryButton("Search");
        searchBtn.setPreferredSize(new Dimension(90, 38));

        controls.add(new JLabel("Filter: "));
        controls.add(statusBox);
        controls.add(searchField);
        controls.add(searchBtn);

        topBar.add(header, BorderLayout.WEST);
        topBar.add(controls, BorderLayout.EAST);
        content.add(topBar, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────────
        String[] cols = {"Type", "Sender VPA", "Receiver VPA", "Amount", "Status", "Note", "Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        UITheme.styleTable(table);

        // Color-coded CREDIT/DEBIT rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                String type = (String) tbl.getValueAt(row, 0);
                boolean credit = "CREDIT".equals(type);
                if (col == 0) {
                    setForeground(credit ? UITheme.ACCENT_GREEN : UITheme.ACCENT_RED);
                    setFont(UITheme.F_SUBHEAD);
                } else if (col == 4) {
                    String st = (String) val;
                    setForeground("SUCCESS".equals(st) ? UITheme.ACCENT_GREEN
                        : "FAILED".equals(st) ? UITheme.ACCENT_RED : UITheme.ACCENT_AMBER);
                } else {
                    setForeground(UITheme.TEXT_DARK);
                    setFont(UITheme.F_BODY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                setBackground(sel ? new Color(79, 142, 247, 40) : (row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252)));
                return this;
            }
        });

        // Fix column widths
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(3).setMaxWidth(120);
        table.getColumnModel().getColumn(4).setMaxWidth(100);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        content.add(scroll, BorderLayout.CENTER);

        // Initial load
        loadData("ALL", "");

        searchBtn.addActionListener(e ->
            loadData((String) statusBox.getSelectedItem(), searchField.getText()));
        searchField.addActionListener(e ->
            loadData((String) statusBox.getSelectedItem(), searchField.getText()));

        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }

    private void loadData(String status, String keyword) {
        tableModel.setRowCount(0);
        List<Transaction> list = TransactionDAO.getAll(user.getVpa(), keyword, status);
        for (Transaction t : list) {
            tableModel.addRow(new Object[]{
                t.getTxnType(),
                t.getSenderVpa(),
                t.getReceiverVpa(),
                String.format("₹ %,.2f", t.getAmount()),
                t.getStatus(),
                t.getNote(),
                t.getFormattedDate()
            });
        }
        if (list.isEmpty()) {
            tableModel.addRow(new Object[]{"—", "No transactions found", "", "", "", "", ""});
        }
    }
}

package ui;

import dao.TransactionDAO;
import dao.UserDAO;
import model.Transaction;
import model.User;
import ui.components.SidebarPanel;
import ui.components.StatCard;
import util.UITheme;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    public DashboardFrame(User user) {
        super("Pay Flow — Dashboard");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);

        User fresh = new UserDAO().getUserById(user.getUserId());
        User u = (fresh != null) ? fresh : user;

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_APP);

        // ── Sidebar ───────────────────────────────────────────────────
        root.add(new SidebarPanel(u, "dashboard", this), BorderLayout.WEST);

        // ── Main content ──────────────────────────────────────────────
        JPanel content = new JPanel(new BorderLayout(0, 24));
        content.setBackground(UITheme.BG_APP);
        content.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        // Header
        JLabel welcome = new JLabel("Good day, " + u.getName() + "!");
        welcome.setFont(UITheme.F_TITLE);
        welcome.setForeground(UITheme.TEXT_DARK);
        content.add(welcome, BorderLayout.NORTH);

        // Centre: stat cards + recent transactions
        JPanel centre = new JPanel(new BorderLayout(0, 24));
        centre.setOpaque(false);

        // Stat cards row
        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        cards.setOpaque(false);
        cards.add(new StatCard("Account Balance",
            String.format("₹ %,.2f", u.getBalance()), UITheme.ACCENT_BLUE));
        cards.add(new StatCard("Total Sent",
            String.format("₹ %,.2f", TransactionDAO.totalSent(u.getVpa())), UITheme.ACCENT_RED));
        cards.add(new StatCard("Total Received",
            String.format("₹ %,.2f", TransactionDAO.totalReceived(u.getVpa())), UITheme.ACCENT_GREEN));
        centre.add(cards, BorderLayout.NORTH);

        // Recent transactions table
        JPanel tableCard = new JPanel(new BorderLayout(0, 10));
        tableCard.setBackground(UITheme.BG_CARD);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel recentLbl = new JLabel("Recent Transactions");
        recentLbl.setFont(UITheme.F_SUBHEAD);
        recentLbl.setForeground(UITheme.TEXT_DARK);
        tableCard.add(recentLbl, BorderLayout.NORTH);

        String[] cols = {"Type", "Counterparty VPA", "Amount", "Status", "Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        List<Transaction> recent = TransactionDAO.getRecent(u.getVpa(), 5);
        for (Transaction t : recent) {
            String counterVpa = t.isCredit() ? t.getSenderVpa() : t.getReceiverVpa();
            model.addRow(new Object[]{
                t.getTxnType(),
                counterVpa,
                String.format("₹ %,.2f", t.getAmount()),
                t.getStatus(),
                t.getFormattedDate()
            });
        }
        JTable table = new JTable(model);
        UITheme.styleTable(table);

        // Color CREDIT green, DEBIT red
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                String type = (String) tbl.getValueAt(row, 0);
                boolean credit = "CREDIT".equals(type);
                setForeground(col == 0 ? (credit ? UITheme.ACCENT_GREEN : UITheme.ACCENT_RED) : UITheme.TEXT_DARK);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scroll, BorderLayout.CENTER);

        if (recent.isEmpty()) {
            JLabel none = new JLabel("No transactions yet", SwingConstants.CENTER);
            none.setFont(UITheme.F_BODY);
            none.setForeground(UITheme.TEXT_MUTED);
            tableCard.add(none, BorderLayout.CENTER);
        }

        centre.add(tableCard, BorderLayout.CENTER);
        content.add(centre, BorderLayout.CENTER);

        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }
}

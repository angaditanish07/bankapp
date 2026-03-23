package ui.components;

import model.User;
import util.UITheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dark sidebar with avatar, user info, and navigation items.
 * Each nav item disposes the parent frame and opens the target screen.
 */
public class SidebarPanel extends JPanel {

    private static final int W = 220;

    public SidebarPanel(User user, String activePage, JFrame parent) {
        setPreferredSize(new Dimension(W, 0));
        setBackground(UITheme.SIDEBAR_BG);
        setLayout(new BorderLayout());

        // ── Top: Avatar + user info ──────────────────────────────────
        JPanel top = new JPanel();
        top.setBackground(UITheme.SIDEBAR_BG);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(28, 20, 20, 20));

        // Avatar circle (painted)
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.ACCENT_BLUE);
                g2.fillOval(0, 0, 56, 56);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                String init = user.getInitials();
                int x = (56 - fm.stringWidth(init)) / 2;
                int y = (56 - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(init, x, y);
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(56, 56));
        avatar.setMaximumSize(new Dimension(56, 56));
        top.add(avatar);
        top.add(Box.createVerticalStrut(10));

        JLabel nameLabel = new JLabel(user.getName());
        nameLabel.setFont(UITheme.F_SUBHEAD);
        nameLabel.setForeground(UITheme.SIDEBAR_TEXT);
        top.add(nameLabel);

        JLabel vpaLabel = new JLabel(user.getVpa());
        vpaLabel.setFont(UITheme.F_SMALL);
        vpaLabel.setForeground(UITheme.SIDEBAR_MUTED);
        top.add(vpaLabel);

        add(top, BorderLayout.NORTH);

        // ── Middle: Nav items ─────────────────────────────────────────
        JPanel nav = new JPanel();
        nav.setBackground(UITheme.SIDEBAR_BG);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 60, 90));
        sep.setMaximumSize(new Dimension(W, 1));
        nav.add(sep);
        nav.add(Box.createVerticalStrut(8));

        nav.add(navItem("  Dashboard",    "dashboard",  activePage, () -> { parent.dispose(); new ui.DashboardFrame(user); }));
        nav.add(navItem("  Send Money",   "sendmoney",  activePage, () -> { parent.dispose(); new ui.SendMoneyFrame(user); }));
        nav.add(navItem("  History",      "history",    activePage, () -> { parent.dispose(); new ui.TransactionHistoryFrame(user); }));
        nav.add(navItem("  Link Bank",    "linkbank",   activePage, () -> { parent.dispose(); new ui.LinkBankFrame(user); }));

        add(nav, BorderLayout.CENTER);

        // ── Bottom: Logout ────────────────────────────────────────────
        JPanel bottom = new JPanel();
        bottom.setBackground(UITheme.SIDEBAR_BG);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        JPanel logoutItem = navItem("  Logout", "logout", activePage,
            () -> { parent.dispose(); new ui.LoginFrame(); });
        bottom.add(logoutItem);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel navItem(String label, String page, String activePage, Runnable action) {
        boolean active = page.equals(activePage);
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        item.setMaximumSize(new Dimension(W, 48));
        item.setBackground(active ? UITheme.SIDEBAR_ACTIVE : UITheme.SIDEBAR_BG);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.F_NAV);
        lbl.setForeground(active ? Color.WHITE : UITheme.SIDEBAR_TEXT);
        item.add(lbl);

        if (!active) {
            item.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    item.setBackground(UITheme.SIDEBAR_HOVER);
                }
                @Override public void mouseExited(MouseEvent e) {
                    item.setBackground(UITheme.SIDEBAR_BG);
                }
                @Override public void mouseClicked(MouseEvent e) {
                    action.run();
                }
            });
        }
        return item;
    }
}

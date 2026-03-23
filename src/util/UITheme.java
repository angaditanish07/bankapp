package util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class UITheme {

    // ─── Colors ─────────────────────────────────────────────────────
    public static final Color SIDEBAR_BG     = new Color(26, 26, 46);
    public static final Color SIDEBAR_ACTIVE = new Color(79, 142, 247);
    public static final Color SIDEBAR_HOVER  = new Color(45, 45, 75);
    public static final Color SIDEBAR_TEXT   = new Color(210, 220, 235);
    public static final Color SIDEBAR_MUTED  = new Color(140, 150, 170);

    public static final Color BG_APP  = new Color(240, 244, 248);
    public static final Color BG_CARD = Color.WHITE;

    public static final Color ACCENT_BLUE  = new Color(79, 142, 247);
    public static final Color ACCENT_GREEN = new Color(16, 185, 129);
    public static final Color ACCENT_RED   = new Color(239, 68, 68);
    public static final Color ACCENT_AMBER = new Color(245, 158, 11);

    public static final Color TEXT_DARK  = new Color(17, 24, 39);
    public static final Color TEXT_MUTED = new Color(107, 114, 128);
    public static final Color TEXT_LIGHT = new Color(156, 163, 175);

    public static final Color BORDER_COLOR = new Color(229, 231, 235);
    public static final Color FIELD_BG     = new Color(249, 250, 251);

    // ─── Fonts ──────────────────────────────────────────────────────
    public static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font F_HEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font F_SUBHEAD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font F_BODY    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font F_BUTTON  = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font F_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_NAV     = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font F_MONO    = new Font("Consolas", Font.PLAIN, 14);

    // ─── Button Factories ────────────────────────────────────────────
    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(ACCENT_BLUE);
        b.setForeground(Color.WHITE);
        b.setFont(F_BUTTON);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(220, 44));
        return b;
    }

    public static JButton secondaryButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(FIELD_BG);
        b.setForeground(TEXT_DARK);
        b.setFont(F_BUTTON);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(220, 44));
        return b;
    }

    public static JButton dangerButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(ACCENT_RED);
        b.setForeground(Color.WHITE);
        b.setFont(F_BUTTON);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(220, 44));
        return b;
    }

    // ─── Field Factories ─────────────────────────────────────────────
    public static JTextField textField() {
        JTextField f = new JTextField();
        f.setFont(F_BODY);
        f.setBackground(FIELD_BG);
        f.setForeground(TEXT_DARK);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        f.setPreferredSize(new Dimension(300, 42));
        return f;
    }

    public static JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(F_BODY);
        f.setBackground(FIELD_BG);
        f.setForeground(TEXT_DARK);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        f.setPreferredSize(new Dimension(300, 42));
        return f;
    }

    public static JLabel errorLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(F_SMALL);
        l.setForeground(ACCENT_RED);
        return l;
    }

    // ─── Panel / Card ─────────────────────────────────────────────────
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));
        return p;
    }

    // ─── Table styling ───────────────────────────────────────────────
    public static void styleTable(JTable t) {
        t.setFont(F_BODY);
        t.setRowHeight(40);
        t.setGridColor(BORDER_COLOR);
        t.setSelectionBackground(new Color(79, 142, 247, 50));
        t.setSelectionForeground(TEXT_DARK);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.getTableHeader().setFont(F_SUBHEAD);
        t.getTableHeader().setBackground(BG_APP);
        t.getTableHeader().setForeground(TEXT_MUTED);
        t.getTableHeader().setPreferredSize(new Dimension(0, 40));
    }
}

package ui.components;

import util.UITheme;
import javax.swing.*;
import java.awt.*;

/** Reusable dashboard stat card — shows a title and a big value. */
public class StatCard extends JPanel {

    public StatCard(String title, String value, Color valueColor) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UITheme.BG_CARD);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));
        setPreferredSize(new Dimension(200, 110));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(UITheme.F_LABEL);
        titleLbl.setForeground(UITheme.TEXT_MUTED);
        titleLbl.setAlignmentX(LEFT_ALIGNMENT);

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLbl.setForeground(valueColor);
        valueLbl.setAlignmentX(LEFT_ALIGNMENT);

        add(titleLbl);
        add(Box.createVerticalStrut(8));
        add(valueLbl);
    }

    public StatCard(String title, String value) {
        this(title, value, UITheme.TEXT_DARK);
    }
}

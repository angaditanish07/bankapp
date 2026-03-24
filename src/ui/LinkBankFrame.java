package ui;

import model.BankAccount;
import model.User;
import service.BankService;
import ui.components.SidebarPanel;
import util.UITheme;
import util.Validator;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LinkBankFrame extends JFrame {

    private final JTextField accNumField  = UITheme.textField();
    private final JTextField bankNameField = UITheme.textField();
    private final JTextField balanceField  = UITheme.textField();
    private final JLabel     accNumErr    = UITheme.errorLabel();
    private final JLabel     bankNameErr  = UITheme.errorLabel();
    private final JLabel     balanceErr   = UITheme.errorLabel();

    private final User        user;
    private final BankService bankService = new BankService();
    private       DefaultListModel<BankAccount> listModel;

    public LinkBankFrame(User user) {
        super("Pay Flow — Link Bank Account");
        this.user = user;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_APP);
        root.add(new SidebarPanel(user, "linkbank", this), BorderLayout.WEST);

        JPanel content = new JPanel(new BorderLayout(0, 24));
        content.setBackground(UITheme.BG_APP);
        content.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        JLabel header = new JLabel("Link Bank Account");
        header.setFont(UITheme.F_TITLE);
        header.setForeground(UITheme.TEXT_DARK);
        content.add(header, BorderLayout.NORTH);

        JPanel centre = new JPanel(new GridLayout(1, 2, 24, 0));
        centre.setOpaque(false);

        // ── Left card: link form ─────────────────────────────────────
        JPanel form = UITheme.card();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel formTitle = new JLabel("Add New Account");
        formTitle.setFont(UITheme.F_SUBHEAD);
        formTitle.setForeground(UITheme.TEXT_DARK);
        formTitle.setAlignmentX(LEFT_ALIGNMENT);

        JLabel infoLbl = new JLabel("Account will be linked to: " + user.getEmail());
        infoLbl.setFont(UITheme.F_SMALL);
        infoLbl.setForeground(UITheme.TEXT_MUTED);
        infoLbl.setAlignmentX(LEFT_ALIGNMENT);

        for (JTextField f : new JTextField[]{accNumField, bankNameField, balanceField})
            f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton linkBtn = UITheme.primaryButton("Link Account");
        linkBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        form.add(formTitle); form.add(Box.createVerticalStrut(4));
        form.add(infoLbl);   form.add(Box.createVerticalStrut(20));
        form.add(fieldRow("Account Number", accNumField,  accNumErr));
        form.add(fieldRow("Bank Name",      bankNameField, bankNameErr));
        form.add(fieldRow("Initial Balance (₹)", balanceField, balanceErr));
        form.add(Box.createVerticalStrut(16));
        form.add(linkBtn);

        linkBtn.addActionListener(e -> doLink());
        centre.add(form);

        // ── Right card: existing accounts ────────────────────────────
        JPanel existingCard = UITheme.card();
        existingCard.setLayout(new BorderLayout(0, 10));

        JLabel existTitle = new JLabel("Linked Accounts");
        existTitle.setFont(UITheme.F_SUBHEAD);
        existTitle.setForeground(UITheme.TEXT_DARK);

        listModel = new DefaultListModel<>();
        JList<BankAccount> accountList = new JList<>(listModel);
        accountList.setFont(UITheme.F_BODY);
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountList.setFixedCellHeight(44);
        accountList.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        refreshList();

        JButton setPrimaryBtn = UITheme.secondaryButton("Set as Primary");
        setPrimaryBtn.addActionListener(e -> {
            BankAccount sel = accountList.getSelectedValue();
            if (sel == null) { JOptionPane.showMessageDialog(this, "Select an account first."); return; }
            bankService.setPrimary(sel.getAccountId(), user.getUserId());
            refreshList();
            JOptionPane.showMessageDialog(this, "Primary account updated.", "Done", JOptionPane.INFORMATION_MESSAGE);
        });

        existingCard.add(existTitle, BorderLayout.NORTH);
        existingCard.add(new JScrollPane(accountList), BorderLayout.CENTER);
        existingCard.add(setPrimaryBtn, BorderLayout.SOUTH);
        centre.add(existingCard);

        content.add(centre, BorderLayout.CENTER);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }

    private void doLink() {
        String acc  = accNumField.getText().trim();
        String bank = bankNameField.getText().trim();
        String bal  = balanceField.getText().trim();
        boolean ok  = true;

        if (acc.isEmpty())  { accNumErr.setText("Account number required"); ok = false; } else accNumErr.setText(" ");
        if (bank.isEmpty()) { bankNameErr.setText("Bank name required");     ok = false; } else bankNameErr.setText(" ");
        String aErr = Validator.amountError(bal);
        if (aErr != null)   { balanceErr.setText(aErr); ok = false; } else balanceErr.setText(" ");
        if (!ok) return;

        BankAccount account = new BankAccount(acc, bank, Double.parseDouble(bal), user.getUserId());
        if (bankService.linkAccount(account)) {
            JOptionPane.showMessageDialog(this, "Bank account linked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            accNumField.setText(""); bankNameField.setText(""); balanceField.setText("");
            refreshList();
        } else {
            JOptionPane.showMessageDialog(this, "Failed — account number may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshList() {
        listModel.clear();
        bankService.getAccounts(user.getUserId()).forEach(listModel::addElement);
    }

    private JPanel fieldRow(String label, JComponent field, JLabel err) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false); p.setAlignmentX(LEFT_ALIGNMENT);
        JLabel l = new JLabel(label); l.setFont(UITheme.F_LABEL); l.setForeground(UITheme.TEXT_MUTED);
        l.setAlignmentX(LEFT_ALIGNMENT); field.setAlignmentX(LEFT_ALIGNMENT); err.setAlignmentX(LEFT_ALIGNMENT);
        p.add(l); p.add(Box.createVerticalStrut(3)); p.add(field); p.add(err); p.add(Box.createVerticalStrut(8));
        return p;
    }
}

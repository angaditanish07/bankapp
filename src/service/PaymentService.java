package service;

import dao.AccountDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import model.BankAccount;
import model.User;
import state.*;
import util.AppLogger;
import util.DBConnection;
import java.sql.*;

public class PaymentService {

    public enum Result {
        SUCCESS, WRONG_PIN, INVALID_RECEIVER, INSUFFICIENT_FUNDS, NO_BANK_LINKED, FAILED;

        public String message() {
            switch (this) {
                case WRONG_PIN:          return "Incorrect UPI PIN. Transaction cancelled.";
                case INVALID_RECEIVER:   return "Receiver VPA not found.";
                case INSUFFICIENT_FUNDS: return "Insufficient balance in your account.";
                case NO_BANK_LINKED:     return "No bank account linked to your profile.";
                case FAILED:             return "Transaction failed. Please try again.";
                default:                 return "Payment successful!";
            }
        }
    }

    private final UserDAO    userDAO    = new UserDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    public Result transfer(User sender, String receiverVpa, double amount, String pin, String note) {

        // 1. Verify PIN
        if (!sender.verifyPin(pin)) return Result.WRONG_PIN;

        // 2. Get receiver
        User receiver = userDAO.getUserByVpa(receiverVpa);
        if (receiver == null) return Result.INVALID_RECEIVER;

        // 3. Sender must have a linked bank account
        BankAccount senderBank = accountDAO.getPrimary(sender.getUserId());
        if (senderBank == null) return Result.NO_BANK_LINKED;

        // 4. Sufficient balance
        if (sender.getBalance() < amount) return Result.INSUFFICIENT_FUNDS;

        // 5. State machine
        TransactionContext ctx = new TransactionContext(new InitiatedState());
        ctx.process();

        Connection conn = null;
        try {
            conn = DBConnection.getNewConnection();
            conn.setAutoCommit(false);

            ctx.setState(new PendingState()); ctx.process();

            double newSenderBal   = sender.getBalance() - amount;
            double newReceiverBal = receiver.getBalance() + amount;

            // Deduct from sender
            PreparedStatement ps1 = conn.prepareStatement(
                "UPDATE users SET balance=? WHERE user_id=?");
            ps1.setDouble(1, newSenderBal); ps1.setInt(2, sender.getUserId());
            ps1.executeUpdate();

            // Credit receiver
            PreparedStatement ps2 = conn.prepareStatement(
                "UPDATE users SET balance=? WHERE user_id=?");
            ps2.setDouble(1, newReceiverBal); ps2.setInt(2, receiver.getUserId());
            ps2.executeUpdate();

            // Save transaction record
            String n = (note == null || note.trim().isEmpty()) ? "UPI Transfer" : note.trim();
            new TransactionDAO().save(conn, sender.getVpa(), receiverVpa, amount, "SUCCESS", n);

            conn.commit();

            // Update sender balance in memory
            sender.setBalance(newSenderBal);

            ctx.setState(new SuccessState()); ctx.process();
            AppLogger.info("Transfer OK: " + sender.getVpa() + " -> " + receiverVpa + " ₹" + amount);
            return Result.SUCCESS;

        } catch (Exception e) {
            ctx.setState(new FailedState()); ctx.process();
            AppLogger.error(e);
            try { if (conn != null) conn.rollback(); } catch (Exception ex) { AppLogger.error(ex); }
            return Result.FAILED;
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) { /* ignore */ }
        }
    }
}

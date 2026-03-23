 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// import state classes
import module5_state.TransactionContext;
import module5_state.InitiatedState;
import module5_state.FailedState;

public class PaymentService {

    public static boolean sendMoney(String sender, String receiver, double amount) {

        try {

            // Start transaction state flow
            TransactionContext context = new TransactionContext(new InitiatedState());
            context.process();

            Connection conn = DBConnection.getConnection();

            // Check sender balance
            String checkBalance = "SELECT balance FROM bank_accounts WHERE user_email=?";
            PreparedStatement stmt1 = conn.prepareStatement(checkBalance);
            stmt1.setString(1, sender);

            ResultSet rs = stmt1.executeQuery();

            if (rs.next()) {

                double balance = rs.getDouble("balance");

                if (balance < amount) {

                    // Transaction failed state
                    TransactionContext failContext = new TransactionContext(new FailedState());
                    failContext.process();

                    return false;
                }

                // Deduct sender balance
                String deduct = "UPDATE bank_accounts SET balance = balance - ? WHERE user_email=?";
                PreparedStatement stmt2 = conn.prepareStatement(deduct);
                stmt2.setDouble(1, amount);
                stmt2.setString(2, sender);
                stmt2.executeUpdate();

                // Add receiver balance
                String add = "UPDATE bank_accounts SET balance = balance + ? WHERE user_email=?";
                PreparedStatement stmt3 = conn.prepareStatement(add);
                stmt3.setDouble(1, amount);
                stmt3.setString(2, receiver);
                stmt3.executeUpdate();

                // Save transaction
                String insert = "INSERT INTO transactions(sender_email, receiver_email, amount, status) VALUES(?,?,?,?)";
                PreparedStatement stmt4 = conn.prepareStatement(insert);

                stmt4.setString(1, sender);
                stmt4.setString(2, receiver);
                stmt4.setDouble(3, amount);
                stmt4.setString(4, "SUCCESS");

                stmt4.executeUpdate();

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
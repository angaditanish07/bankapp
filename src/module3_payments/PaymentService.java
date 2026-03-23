package module3_payments;
import module2_bank.AccountDAO;
import module2_bank.BankAccount;
import module1_authentication.User;
import module1_authentication.UserDAO;
import module4_history.TransactionDAO;

public class PaymentService {

    public boolean transfer(String senderVpa, String receiverVpa, double amount) {

        try {

            UserDAO userDAO = new UserDAO();

            User sender = userDAO.getUserByVpa(senderVpa);
            User receiver = userDAO.getUserByVpa(receiverVpa);

            if (sender == null || receiver == null) {
                return false;
            }

            // 👉 Get accounts using email
            BankAccount senderAcc = AccountDAO.getAccountByEmail(sender.getEmail());
            BankAccount receiverAcc = AccountDAO.getAccountByEmail(receiver.getEmail());

            if (senderAcc == null || receiverAcc == null) {
                return false;
            }

            // 👉 Check balance
            if (senderAcc.getBalance() < amount) {
                return false;
            }

            // 👉 Deduct & Add
            senderAcc.setBalance(senderAcc.getBalance() - amount);
            receiverAcc.setBalance(receiverAcc.getBalance() + amount);

            // 👉 Update DB
            AccountDAO.updateBalance(senderAcc);
            AccountDAO.updateBalance(receiverAcc);

            // 👉 Save transaction
            TransactionDAO txnDAO = new TransactionDAO();
            txnDAO.saveTransaction(senderVpa, receiverVpa, amount, "SUCCESS");

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
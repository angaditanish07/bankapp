package module5_state;

public class PendingState implements TransactionState {

    public void handleTransaction() {
        System.out.println("Transaction Pending");
    }

}
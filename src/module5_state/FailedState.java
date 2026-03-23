package module5_state;

public class FailedState implements TransactionState {

    public void handleTransaction() {
        System.out.println("Transaction Failed");
    }

}
package module5_state;

public class InitiatedState implements TransactionState {

    public void handleTransaction() {
        System.out.println("Transaction Initiated");
    }

}
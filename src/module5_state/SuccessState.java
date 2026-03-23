package module5_state;

public class SuccessState implements TransactionState {

    public void handleTransaction() {
        System.out.println("Transaction Successful");
    }

}
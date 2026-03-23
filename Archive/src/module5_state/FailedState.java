public class FailedState implements TransactionState {

    @Override
    public void process(TransactionContext context) {

        System.out.println("Transaction Failed");

    }

}
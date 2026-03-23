public class SuccessState implements TransactionState {

    @Override
    public void process(TransactionContext context) {

        System.out.println("Transaction Successful");

    }

}
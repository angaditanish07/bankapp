public class PendingState implements TransactionState {

    @Override
    public void process(TransactionContext context) {

        System.out.println("Transaction Pending");

        context.setState(new SuccessState());

        context.process();
    }

}
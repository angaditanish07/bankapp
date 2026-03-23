public class InitiatedState implements TransactionState {

    @Override
    public void process(TransactionContext context) {

        System.out.println("Transaction Initiated");

        context.setState(new PendingState());

        context.process();
    }

}
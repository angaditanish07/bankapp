public class TransactionContext {

    private TransactionState state;

    public TransactionContext(TransactionState state) {
        this.state = state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public void process() {
        state.process(this);
    }

}
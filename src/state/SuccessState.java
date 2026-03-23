package state;
import util.AppLogger;
public class SuccessState implements TransactionState {
    public void handleTransaction() { AppLogger.info("[STATE] Transaction Successful"); }
}

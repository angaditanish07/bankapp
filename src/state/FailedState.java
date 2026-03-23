package state;
import util.AppLogger;
public class FailedState implements TransactionState {
    public void handleTransaction() { AppLogger.info("[STATE] Transaction Failed"); }
}

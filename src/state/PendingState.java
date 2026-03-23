package state;
import util.AppLogger;
public class PendingState implements TransactionState {
    public void handleTransaction() { AppLogger.info("[STATE] Transaction Pending"); }
}

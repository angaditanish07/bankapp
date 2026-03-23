package state;
import util.AppLogger;
public class InitiatedState implements TransactionState {
    public void handleTransaction() { AppLogger.info("[STATE] Transaction Initiated"); }
}

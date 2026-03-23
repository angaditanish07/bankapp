import java.util.Date;

public class Transaction {

    private String senderEmail;
    private String receiverEmail;
    private double amount;
    private String status;
    private Date date;

    public Transaction(String senderEmail, String receiverEmail, double amount, String status) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.amount = amount;
        this.status = status;
        this.date = new Date();
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }
}
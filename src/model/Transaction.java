package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

    private String senderVpa;
    private String receiverVpa;
    private double amount;
    private String status;   // SUCCESS | FAILED | PENDING
    private String txnType;  // DEBIT | CREDIT  (computed from reader's perspective)
    private String note;
    private Date   date;

    public Transaction(String senderVpa, String receiverVpa, double amount,
                       String status, String txnType, String note) {
        this.senderVpa   = senderVpa;
        this.receiverVpa = receiverVpa;
        this.amount      = amount;
        this.status      = status;
        this.txnType     = txnType;
        this.note        = (note != null ? note : "");
        this.date        = new Date();
    }

    public String getSenderVpa()   { return senderVpa; }
    public String getReceiverVpa() { return receiverVpa; }
    public double getAmount()      { return amount; }
    public String getStatus()      { return status; }
    public String getTxnType()     { return txnType; }
    public String getNote()        { return note; }
    public Date   getDate()        { return date; }

    public void setDate(Date date) { this.date = date; }

    public String getFormattedDate() {
        if (date == null) return "N/A";
        return new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(date);
    }

    public boolean isCredit() { return "CREDIT".equalsIgnoreCase(txnType); }
}

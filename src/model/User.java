package model;

public class User {

    private int    userId;
    private String name;
    private String mobile;
    private String email;
    private String vpa;
    private String upiPin;
    private double balance;

    public User(int userId, String name, String mobile, String email,
                String vpa, String upiPin, double balance) {
        this.userId  = userId;
        this.name    = name;
        this.mobile  = mobile;
        this.email   = email;
        this.vpa     = vpa;
        this.upiPin  = upiPin;
        this.balance = balance;
    }

    // Getters
    public int    getUserId()  { return userId; }
    public String getName()    { return name; }
    public String getMobile()  { return mobile; }
    public String getEmail()   { return email; }
    public String getVpa()     { return vpa; }
    public String getUpiPin()  { return upiPin; }
    public double getBalance() { return balance; }

    // Setters
    public void setBalance(double balance) { this.balance = balance; }
    public void setUpiPin(String upiPin)   { this.upiPin = upiPin; }

    public boolean verifyPin(String pin) {
        return upiPin != null && upiPin.equals(pin);
    }

    /** Initials for avatar display, e.g. "John Smith" -> "JS" */
    public String getInitials() {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }
}

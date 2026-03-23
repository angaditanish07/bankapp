public class User {

    private int userId;
    private String name;
    private String mobile;
    private String email;
    private String vpa;
    private String upiPin;

    public User(int userId, String name, String mobile, String email, String vpa, String upiPin) {
        this.userId = userId;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.vpa = vpa;
        this.upiPin = upiPin;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getVpa() {
        return vpa;
    }

    public boolean verifyPin(String pin) {
        return upiPin.equals(pin);
    }
}
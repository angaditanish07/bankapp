package util;

import java.util.regex.*;

public class Validator {

    private static final Pattern EMAIL  = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern VPA    = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9]+$");
    private static final Pattern MOBILE = Pattern.compile("^[6-9][0-9]{9}$");
    private static final Pattern PIN    = Pattern.compile("^[0-9]{4,6}$");

    public static boolean isValidEmail(String s)  { return s != null && EMAIL.matcher(s.trim()).matches(); }
    public static boolean isValidVPA(String s)    { return s != null && VPA.matcher(s.trim()).matches(); }
    public static boolean isValidMobile(String s) { return s != null && MOBILE.matcher(s.trim()).matches(); }
    public static boolean isValidPin(String s)    { return s != null && PIN.matcher(s.trim()).matches(); }
    public static boolean isValidAmount(String s) {
        try { return s != null && Double.parseDouble(s.trim()) > 0; }
        catch (NumberFormatException e) { return false; }
    }

    public static String emailError(String s)  { return isValidEmail(s)  ? null : "Enter a valid email (e.g. user@gmail.com)"; }
    public static String vpaError(String s)    { return isValidVPA(s)    ? null : "VPA format: name@bankname"; }
    public static String mobileError(String s) { return isValidMobile(s) ? null : "Enter a valid 10-digit mobile number"; }
    public static String pinError(String s)    { return isValidPin(s)    ? null : "PIN must be 4–6 digits"; }
    public static String amountError(String s) { return isValidAmount(s) ? null : "Enter a valid positive amount"; }
}

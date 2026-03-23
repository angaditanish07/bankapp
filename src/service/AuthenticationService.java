package service;

import dao.UserDAO;
import model.User;
import util.AppLogger;

public class AuthenticationService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String vpa, String pin) {
        if (vpa == null || vpa.isEmpty() || pin == null || pin.isEmpty()) return null;
        User user = userDAO.getUserByVpa(vpa);
        if (user != null && user.verifyPin(pin)) {
            AppLogger.info("Login OK: " + vpa);
            return user;
        }
        AppLogger.warn("Login failed: " + vpa);
        return null;
    }

    public boolean register(User user) {
        return userDAO.registerUser(user);
    }

    public boolean vpaExists(String vpa) {
        return userDAO.vpaExists(vpa);
    }
}

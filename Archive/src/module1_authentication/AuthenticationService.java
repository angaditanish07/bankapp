public class AuthenticationService {

    private UserDAO userDAO = new UserDAO();

    public User login(String vpa, String pin) {

        User user = userDAO.getUserByVpa(vpa);

        if (user != null && user.verifyPin(pin)) {

            return user;

        }

        return null;
    }
}
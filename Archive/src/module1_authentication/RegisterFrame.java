import javax.swing.*;

public class RegisterFrame extends JFrame {

    JTextField nameField = new JTextField();
    JTextField mobileField = new JTextField();
    JTextField emailField = new JTextField();
    JTextField vpaField = new JTextField();

    JButton registerBtn = new JButton("Register");

    public RegisterFrame() {

        setTitle("UPI Register");

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(new JLabel("Name"));
        add(nameField);

        add(new JLabel("Mobile"));
        add(mobileField);

        add(new JLabel("Email"));
        add(emailField);

        add(new JLabel("Create VPA"));
        add(vpaField);

        add(registerBtn);

        registerBtn.addActionListener(e -> registerUser());

        setSize(300,300);
        setVisible(true);
    }

    private void registerUser() {

        User user = new User(
                0,
                nameField.getText(),
                mobileField.getText(),
                emailField.getText(),
                vpaField.getText(),
                "1234"
        );

        UserDAO dao = new UserDAO();

        if(dao.registerUser(user)) {

            JOptionPane.showMessageDialog(this,"User Registered");

        } else {

            JOptionPane.showMessageDialog(this,"Registration Failed");

        }
    }
    public static void main(String[] args) {
    new RegisterFrame();
}
}
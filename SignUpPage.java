import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignUpPage() {
        setTitle("Book My Ticket - Sign Up");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("SignUp");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(140, 20, 150, 30);
        panel.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 70, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 70, 180, 25);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 110, 100, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 110, 180, 25);
        panel.add(passwordField);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setBounds(50, 150, 150, 25);
        panel.add(confirmLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(160, 150, 180, 25);
        panel.add(confirmPasswordField);

        JButton signUpButton = new JButton("SignIn");
        signUpButton.setBounds(150, 190, 100, 30);
        panel.add(signUpButton);

        JButton backButton = new JButton("LogIn");
        backButton.setBounds(260, 190, 80, 30);
        panel.add(backButton);

        add(panel);
        setVisible(true);

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!!!");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match!!!");
                    return;
                }

                boolean success = AuthUtil.signUp(username, password);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Registration successful! Please login again");
                    dispose();
                    new LoginPage();
                } else {
                    JOptionPane.showMessageDialog(null, "User already exists or Something went wrong");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage();
            }
        });
    }

    public static void main(String[] args) {
        new SignUpPage();
    }
}


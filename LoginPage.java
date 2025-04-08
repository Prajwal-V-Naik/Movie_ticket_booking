import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Book My Ticket - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("LogIn");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(150, 20, 100, 30);
        panel.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 70, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 70, 180, 25);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 110, 100, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 110, 180, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("LogIn");
        loginButton.setBounds(150, 150, 90, 30);
        panel.add(loginButton);

        JButton signUpButton = new JButton("SignUp");
        signUpButton.setBounds(250, 150, 90, 30);
        panel.add(signUpButton);

        add(panel);
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (AuthUtil.validateLogin(username, password)) {
                    JOptionPane.showMessageDialog(null, "Logged in Successfully");
                    dispose();
                    new MovieSelectionPage(username);
                } else {
                    JOptionPane.showMessageDialog(null, "Check your username or password is correct?");
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SignUpPage();
            }
        });
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}

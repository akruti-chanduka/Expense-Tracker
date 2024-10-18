import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Frame0 implements ActionListener {
    JFrame frame;
    JPanel loginPanel, registerPanel;
    JButton loginButton, registerButton;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton switchToRegisterButton, switchToLoginButton;

    Frame0() {
        frame = new JFrame("Expense Tracker");
        frame.setSize(400, 300);
        frame.setLocation(300, 300);
        frame.setLayout(new CardLayout());

        loginPanel = new JPanel();
        loginPanel.setLayout(null);

        registerPanel = new JPanel();
        registerPanel.setLayout(null);

        // Login Panel
        setupLoginPanel();

        // Register Panel
        setupRegisterPanel();

        frame.add(loginPanel, "Login");
        frame.add(registerPanel, "Register");

        showLoginPanel();
        
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setupLoginPanel() {
        JLabel heading = new JLabel("Login");
        heading.setBounds(170, 20, 100, 30);
        loginPanel.add(heading);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 70, 100, 30);
        loginPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 70, 150, 30);
        loginPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 120, 100, 30);
        loginPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 150, 30);
        loginPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(80, 200, 100, 30);
        loginButton.addActionListener(this);
        loginPanel.add(loginButton);

        switchToRegisterButton = new JButton("Register");
        switchToRegisterButton.setBounds(200, 200, 100, 30);
        switchToRegisterButton.addActionListener(this);
        loginPanel.add(switchToRegisterButton);
    }

    private void setupRegisterPanel() {
        JLabel heading = new JLabel("Register");
        heading.setBounds(160, 20, 100, 30);
        registerPanel.add(heading);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 70, 100, 30);
        registerPanel.add(usernameLabel);

        JTextField registerUsernameField = new JTextField();
        registerUsernameField.setBounds(150, 70, 150, 30);
        registerPanel.add(registerUsernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 120, 100, 30);
        registerPanel.add(passwordLabel);

        JPasswordField registerPasswordField = new JPasswordField();
        registerPasswordField.setBounds(150, 120, 150, 30);
        registerPanel.add(registerPasswordField);

        registerButton = new JButton("Register");
        registerButton.setBounds(80, 200, 100, 30);
        registerButton.addActionListener(e -> {
            String username = registerUsernameField.getText();
            String password = new String(registerPasswordField.getPassword());
            registerUser(username, password);
        });
        registerPanel.add(registerButton);

        switchToLoginButton = new JButton("Login");
        switchToLoginButton.setBounds(200, 200, 100, 30);
        switchToLoginButton.addActionListener(e -> showLoginPanel());
        registerPanel.add(switchToLoginButton);
    }

    private void showLoginPanel() {
        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), "Login");
    }

    private void showRegisterPanel() {
        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), "Register");
    }

    public void actionPerformed(ActionEvent ae) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (ae.getSource().equals(loginButton)) {
            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful");
                frame.dispose();
                new Frame1(username); // Pass username to the next frame
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Username or Password");
            }
        } else if (ae.getSource().equals(switchToRegisterButton)) {
            showRegisterPanel();
        }
    }

    private boolean authenticateUser(String username, String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "12345");
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            boolean valid = rs.next();
            rs.close();
            stmt.close();
            con.close();
            return valid;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void registerUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username and Password cannot be empty.");
            return;
        }

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "12345");

            // Check if username already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            int count = checkRs.getInt(1);
            checkRs.close();
            checkStmt.close();

            if (count > 0) {
                JOptionPane.showMessageDialog(frame, "Username already exists. Please choose a different username.");
                con.close();
                return;
            }

            // Insert new user
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "Registration Successful");
                showLoginPanel(); // Switch back to login panel after successful registration
            } else {
                JOptionPane.showMessageDialog(frame, "Registration Failed");
            }

            stmt.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Frame0();
    }
}

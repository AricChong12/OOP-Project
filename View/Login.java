package View;

import javax.swing.*;
import java.awt.*;
import Controller.CustomerController;
import Model.Customer;

public class Login extends JPanel {
    public Login(MainFrame mainFrame) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("Login Portal");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; add(lblTitle, gbc);

        gbc.gridy = 1; add(new JLabel("IC Number"), gbc);
        JTextField txtIC = new JTextField(20);
        gbc.gridy = 2; add(txtIC, gbc);

        gbc.gridy = 3; add(new JLabel("Password"), gbc);
        JPasswordField txtPass = new JPasswordField(20);
        gbc.gridy = 4; add(txtPass, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> {
            CustomerController cc = new CustomerController();
            Customer user = cc.loginCustomer(txtIC.getText(), new String(txtPass.getPassword()));
            
            if (user != null) {
                MainFrame.currentCustomer = user;
                mainFrame.showScreen("RoomStatus");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid IC or Password!");
            }
        });
        
        gbc.gridy = 5; add(btnLogin, gbc);
        
        JButton btnReg = new JButton("No account? Register");
        btnReg.addActionListener(e -> mainFrame.showScreen("Register"));
        gbc.gridy = 6; add(btnReg, gbc);
    }
}
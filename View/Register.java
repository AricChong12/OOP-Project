package View;

import javax.swing.*;
import java.awt.*;
import Model.Customer;
import Controller.CustomerController;

public class Register extends JPanel {
    private JTextField txtName, txtIC, txtPhone, txtEmail;
    private JPasswordField txtPass;

    public Register(MainFrame mainFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Full Name:"), gbc);
        txtName = new JTextField(20); gbc.gridx = 1; add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("IC Number:"), gbc);
        txtIC = new JTextField(20); gbc.gridx = 1; add(txtIC, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Phone:"), gbc);
        txtPhone = new JTextField(20); gbc.gridx = 1; add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20); gbc.gridx = 1; add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Password:"), gbc);
        txtPass = new JPasswordField(20); gbc.gridx = 1; add(txtPass, gbc);

        JButton btnSave = new JButton("Register");
        btnSave.addActionListener(e -> {
            Customer c = new Customer(txtName.getText(), txtIC.getText(), 
                                      txtPhone.getText(), txtEmail.getText(), 
                                      new String(txtPass.getPassword()));
            
            CustomerController cc = new CustomerController();
            if (cc.registerCustomer(c)) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                mainFrame.showScreen("Login");
            } else {
                JOptionPane.showMessageDialog(this, "Error saving to database.");
            }
        });

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; add(btnSave, gbc);
    }
}
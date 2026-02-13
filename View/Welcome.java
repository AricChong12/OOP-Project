package View;

import javax.swing.*;
import java.awt.*;

public class Welcome extends JPanel {
    public Welcome(MainFrame mainFrame) {
        setLayout(new GridBagLayout());
        setBackground(new Color(45, 45, 45));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel lblTitle = new JLabel("WELCOME TO HOTEL PRO");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; add(lblTitle, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(200, 50));
        btnLogin.addActionListener(e -> mainFrame.showScreen("Login"));
        gbc.gridy = 1; add(btnLogin, gbc);

        JButton btnReg = new JButton("Register New Account");
        btnReg.setPreferredSize(new Dimension(200, 50));
        btnReg.addActionListener(e -> mainFrame.showScreen("Register"));
        gbc.gridy = 2; add(btnReg, gbc);
    }
}
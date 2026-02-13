package View;

import javax.swing.*;
import java.awt.*;

public class Success extends JPanel {
    public Success(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        
        JLabel msg = new JLabel("Transaction Completed Successfully!", SwingConstants.CENTER);
        msg.setFont(new Font("Arial", Font.BOLD, 20));
        add(msg, BorderLayout.CENTER);

        JButton btnHome = new JButton("Return to Room Status");
        btnHome.addActionListener(e -> mainFrame.showScreen("RoomStatus"));
        add(btnHome, BorderLayout.SOUTH);
    }
}
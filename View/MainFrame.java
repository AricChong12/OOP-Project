package View;

import javax.swing.*;
import java.awt.*;
import Model.Customer;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    
    
    private CheckIn checkInPanel; 
    private RoomStatus roomStatusPanel; 
    private CheckOut checkOutPanel; 

    // Global Session Data
    public static Customer currentCustomer; 
    public static String selectedRoomNumber = "";
    public static String selectedRoomType = "";
    public static double selectedRoomPrice = 0.0;

    public MainFrame() {
        setTitle("Hotel Management System - Integrated");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize Panels & assign to variables
        checkInPanel = new CheckIn(this);
        roomStatusPanel = new RoomStatus(this);
        checkOutPanel = new CheckOut(this); 

        // Add them to the layout using the variables
        mainPanel.add(new Login(this), "Login");
        mainPanel.add(new Register(this), "Register");
        mainPanel.add(roomStatusPanel, "RoomStatus"); 
        mainPanel.add(checkInPanel, "CheckIn");       
        mainPanel.add(checkOutPanel, "CheckOut"); 

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    public void showScreen(String screenName) {
        
        
        if (screenName.equals("CheckIn")) {
            // Force CheckIn to fetch the new Room ID
            checkInPanel.updateFormDetails(); 
        } 
        else if (screenName.equals("RoomStatus")) {
            // Force RoomStatus to fetch the new Red/Green colors
            roomStatusPanel.refreshGrid(); 
        }
        else if (screenName.equals("CheckOut")) {
            // Force CheckOut to calculate the bill
            checkOutPanel.updateBillingDetails(); 
        }

        cardLayout.show(mainPanel, screenName);
    }
}
package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import Controller.RoomController;
import Model.Room;
import java.util.List;

public class RoomStatus extends JPanel {
    private JPanel grid;
    private MainFrame mainFrame;

    public RoomStatus(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        
        // HEADER SECTION
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding
        topPanel.setBackground(new Color(240, 240, 240)); // light gray header

        // Title (Center)
        JLabel title = new JLabel("Live Room Status", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        // Logout Button (Right)
        JButton btnLogout = new JButton("Logout");
        styleButton(btnLogout, new Color(220, 53, 69)); // Red color
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Smaller font for header
        btnLogout.setPreferredSize(new Dimension(90, 30)); // Smaller size
        
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Clear Session
                MainFrame.currentCustomer = null;
                MainFrame.selectedRoomNumber = "";
                MainFrame.selectedRoomType = "";
                MainFrame.selectedRoomPrice = 0.0;
                // Redirect to Login
                mainFrame.showScreen("Login");
            }
        });

        // Wrap button in a FlowLayout to prevent it from stretching vertically
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        logoutPanel.setOpaque(false); // Transparent background
        logoutPanel.add(btnLogout);
        
        topPanel.add(logoutPanel, BorderLayout.EAST);
        
        // Add Header to Main Layout
        add(topPanel, BorderLayout.NORTH);


        // ROOM GRID (Center) 
        grid = new JPanel(new GridLayout(0, 4, 15, 15)); 
        grid.setBorder(new EmptyBorder(10, 20, 10, 20)); 
        add(new JScrollPane(grid), BorderLayout.CENTER);
        
        
        // FOOTER SECTION (Bottom) 
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton btnRefresh = new JButton("Refresh Status");
        styleButton(btnRefresh, new Color(40, 90, 160)); // Blue
        btnRefresh.setPreferredSize(new Dimension(160, 40)); // Standard size
        btnRefresh.addActionListener(e -> refreshGrid());
        
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Auto-refresh when screen loads
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshGrid();
            }
        });
        
        // Initial Load
        refreshGrid();
    }

    public void refreshGrid() {
        grid.removeAll();
        RoomController rc = new RoomController();
        List<Room> rooms = rc.getAllRooms();

        for (Room r : rooms) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            
            // Color Logic
            if ("Occupied".equalsIgnoreCase(r.getStatus())) {
                card.setBackground(new Color(255, 100, 100)); // Red
            } else {
                card.setBackground(new Color(144, 238, 144)); // Green
            }
            
            JLabel lblID = new JLabel("Room " + r.getRoomID(), SwingConstants.CENTER);
            lblID.setFont(new Font("Segoe UI", Font.BOLD, 14));
            card.add(lblID, BorderLayout.NORTH);
            
            JLabel lblType = new JLabel(r.getType(), SwingConstants.CENTER);
            card.add(lblType, BorderLayout.CENTER);
            
            // Booking Logic
            if ("Available".equalsIgnoreCase(r.getStatus())) {
                JButton btnSelect = new JButton("Book Now");
                btnSelect.setBackground(Color.WHITE);
                btnSelect.setFocusPainted(false);
                
                btnSelect.addActionListener(e -> {
                    MainFrame.selectedRoomNumber = String.valueOf(r.getRoomID());
                    MainFrame.selectedRoomType = r.getType();
                    
                    String type = r.getType().toLowerCase();
                    if (type.contains("suite") || type.contains("king")) {
                        MainFrame.selectedRoomPrice = 300.0;
                    } else if (type.contains("deluxe") || type.contains("queen")) {
                        MainFrame.selectedRoomPrice = 200.0;
                    } else {
                        MainFrame.selectedRoomPrice = 100.0; 
                    }

                    mainFrame.showScreen("CheckIn");
                });
                card.add(btnSelect, BorderLayout.SOUTH);
            }
            grid.add(card);
        }
        grid.revalidate();
        grid.repaint();
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(5, 15, 5, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
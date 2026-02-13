package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.time.LocalDate;
import Controller.BookingController;
import Database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CheckIn extends JPanel {
    // UI Components
    private JTextField txtRoomNum, txtName, txtIC, txtPhone, txtEmail;
    private JComboBox<String> cbGuests;
    private JComboBox<String> cbDayIn, cbMonthIn, cbYearIn;
    private JComboBox<String> cbDayOut, cbMonthOut, cbYearOut;

    private MainFrame mainFrame;

    public CheckIn(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // AUTO-UPDATE LISTENER
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                System.out.println("Opening CheckIn Screen - Auto-filling data...");
                updateFormDetails(); // This function fills the text fields
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) { }
            @Override
            public void ancestorMoved(AncestorEvent event) { }
        });

        // HEADER 
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("New Booking - Room Check-in");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(50, 50, 50));

        JButton btnBack = new JButton("Go to Room Plan");
        styleButton(btnBack, new Color(40, 90, 160)); // Dark Blue
        btnBack.addActionListener(e -> mainFrame.showScreen("RoomStatus"));

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnBack, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // MAIN FORM 
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // LEFT COLUMN: GUEST INFO
        
        // Room Number Section
        addLabel(formPanel, "Room Number", 0, 0, gbc);
        txtRoomNum = new JTextField();
        txtRoomNum.setEditable(false); // Read-only
        txtRoomNum.setBackground(new Color(245, 245, 245));
        styleTextField(txtRoomNum);
        addComponent(formPanel, txtRoomNum, 0, 1, gbc);

        // Header: Guest Information
        JLabel lblGuestHeader = new JLabel("Guest Information");
        lblGuestHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(20, 15, 5, 15);
        formPanel.add(lblGuestHeader, gbc);

        // Fields
        gbc.insets = new Insets(5, 15, 5, 15);
        addLabel(formPanel, "Full Name", 0, 3, gbc);
        txtName = new JTextField(); styleTextField(txtName);
        addComponent(formPanel, txtName, 0, 4, gbc);

        addLabel(formPanel, "IC Number", 0, 5, gbc);
        txtIC = new JTextField(); styleTextField(txtIC);
        addComponent(formPanel, txtIC, 0, 6, gbc);

        addLabel(formPanel, "Contact Number", 0, 7, gbc);
        txtPhone = new JTextField(); styleTextField(txtPhone);
        addComponent(formPanel, txtPhone, 0, 8, gbc);

        addLabel(formPanel, "Email", 0, 9, gbc);
        txtEmail = new JTextField(); styleTextField(txtEmail);
        addComponent(formPanel, txtEmail, 0, 10, gbc);

        //RIGHT COLUMN: BOOKING DETAILS 
        
        // Header: Booking Details
        JLabel lblBookingHeader = new JLabel("Booking Details");
        lblBookingHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 1; gbc.gridy = 0; gbc.insets = new Insets(10, 15, 5, 15);
        formPanel.add(lblBookingHeader, gbc);

        // Dates Row
        JPanel dateRow = new JPanel(new GridLayout(1, 2, 20, 0));
        dateRow.setBackground(Color.WHITE);
        
        JPanel pnlIn = new JPanel(new BorderLayout()); 
        pnlIn.setBackground(Color.WHITE);
        pnlIn.add(new JLabel("Check-in Date"), BorderLayout.NORTH);
        pnlIn.add(createDatePanel(true), BorderLayout.CENTER);
        
        JPanel pnlOut = new JPanel(new BorderLayout()); 
        pnlOut.setBackground(Color.WHITE);
        pnlOut.add(new JLabel("Check-out Date"), BorderLayout.NORTH);
        pnlOut.add(createDatePanel(false), BorderLayout.CENTER);

        dateRow.add(pnlIn);
        dateRow.add(pnlOut);

        gbc.gridx = 1; gbc.gridy = 1; 
        formPanel.add(dateRow, gbc);

        // Number of Guests
        gbc.gridy = 2;
        addLabel(formPanel, "Number of Guests", 1, 2, gbc);
        String[] guests = {"1 Guest", "2 Guests", "3 Guests", "4 Guests"};
        cbGuests = new JComboBox<>(guests);
        cbGuests.setBackground(Color.WHITE);
        addComponent(formPanel, cbGuests, 1, 3, gbc);

        //BUTTONS (Bottom Right) 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnCancel = new JButton("Cancel");
        styleButton(btnCancel, new Color(200, 200, 200));
        btnCancel.setForeground(Color.BLACK);
        btnCancel.addActionListener(e -> mainFrame.showScreen("RoomStatus"));

        JButton btnConfirm = new JButton("Confirm Check-in");
        styleButton(btnConfirm, new Color(40, 167, 69)); // Green
       
        //CONFIRM BUTTON LOGIC 
        btnConfirm.addActionListener(e -> {
            try {
                LocalDate in = getDateFromCombos(cbDayIn, cbMonthIn, cbYearIn);
                LocalDate out = getDateFromCombos(cbDayOut, cbMonthOut, cbYearOut);

                if (!out.isAfter(in)) {
                    JOptionPane.showMessageDialog(this, "Check-out must be after Check-in!");
                    return;
                }

                BookingController bc = new BookingController();
                boolean success = bc.createBooking(MainFrame.currentCustomer, MainFrame.selectedRoomNumber, in.toString(), out.toString());

                if (success) {
                    updateRoomStatus(MainFrame.selectedRoomNumber, "Occupied");
                    
                    MainFrame.currentCustomer.setCheckInDate(in.toString());
                    MainFrame.currentCustomer.setCheckOutDate(out.toString());
                    
                    JOptionPane.showMessageDialog(this, "Booking Successful! Proceeding to Billing...");
                    
                    
                    mainFrame.showScreen("CheckOut"); 
                } else {
                    JOptionPane.showMessageDialog(this, "Booking Failed! Check Database connection.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid Date Selected!");
            }
        });

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnConfirm);

        gbc.gridx = 1; gbc.gridy = 10; 
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        formPanel.add(buttonPanel, gbc);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);
    }

    //LOGICS

    public void updateRoomDisplay() {
        updateFormDetails();
    }

    public void updateFormDetails() {
        // Fill Room Number
        if (MainFrame.selectedRoomNumber != null && !MainFrame.selectedRoomNumber.isEmpty()) {
            txtRoomNum.setText(MainFrame.selectedRoomNumber + " - " + MainFrame.selectedRoomType);
        } else {
            txtRoomNum.setText("(No Room Selected)");
        }

        // Fill User Info
        if (MainFrame.currentCustomer != null) {
            txtName.setText(MainFrame.currentCustomer.getName());
            txtIC.setText(MainFrame.currentCustomer.getIC());
            
            
            txtPhone.setText(MainFrame.currentCustomer.getPhoneNo());
            
            txtEmail.setText(MainFrame.currentCustomer.getEmail());
        }
    }

    // Turn room red
    private void updateRoomStatus(String roomId, String status) {
        String sql = "UPDATE room SET status = ? WHERE room_id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, status); // "Occupied"
            pst.setString(2, roomId);
            pst.executeUpdate();
            
            System.out.println("DEBUG: Room " + roomId + " forcibly updated to " + status);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // UI methods

    private void addLabel(JPanel panel, String text, int x, int y, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(Color.GRAY);
        gbc.gridx = x; gbc.gridy = y;
        panel.add(label, gbc);
    }

    private void addComponent(JPanel panel, JComponent comp, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x; gbc.gridy = y;
        comp.setPreferredSize(new Dimension(200, 35));
        panel.add(comp, gbc);
    }

    private void styleTextField(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createDatePanel(boolean isCheckIn) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        panel.setBackground(Color.WHITE);
        
        JComboBox<String> day = new JComboBox<>();
        for(int i=1; i<=31; i++) day.addItem(String.format("%02d", i));

        JComboBox<String> month = new JComboBox<>();
        for(int i=1; i<=12; i++) month.addItem(String.format("%02d", i));

        JComboBox<String> year = new JComboBox<>();
        for(int i=2025; i<=2030; i++) year.addItem(String.valueOf(i));

        panel.add(day); panel.add(new JLabel("/"));
        panel.add(month); panel.add(new JLabel("/"));
        panel.add(year);

        if (isCheckIn) {
            cbDayIn = day; cbMonthIn = month; cbYearIn = year;
        } else {
            cbDayOut = day; cbMonthOut = month; cbYearOut = year;
        }
        return panel;
    }

    private LocalDate getDateFromCombos(JComboBox<String> d, JComboBox<String> m, JComboBox<String> y) {
        int day = Integer.parseInt((String) d.getSelectedItem());
        int month = Integer.parseInt((String) m.getSelectedItem());
        int year = Integer.parseInt((String) y.getSelectedItem());
        return LocalDate.of(year, month, day);
    }
}
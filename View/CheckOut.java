package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.print.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import Controller.PaymentController;
import Database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CheckOut extends JPanel {
    // UI Labels
    private JLabel lblTitle, lblGuestName, lblRoom, lblCheckIn, lblCheckOut;
    private JLabel lblDesc, lblDate, lblQty, lblRate, lblAmount, lblTotal;
    
    private JPanel centerPanel; 
    private MainFrame mainFrame;

    public CheckOut(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 248, 250)); 
        setBorder(new EmptyBorder(30, 50, 30, 50)); 

        // --- TITLE ---
        lblTitle = new JLabel("Booking Confirmation & Payment"); // Changed Title to match logic
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 55, 80));
        add(lblTitle, BorderLayout.NORTH);

        // --- CENTER PANEL ---
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(245, 248, 250));
        centerPanel.add(Box.createVerticalStrut(20)); 

        // === CARD 1: GUEST INFO ===
        JPanel pnlGuest = createCardPanel();
        pnlGuest.setLayout(new GridLayout(2, 3, 10, 20)); 
        
        pnlGuest.add(createField("Guest Name", lblGuestName = new JLabel("-")));
        pnlGuest.add(createField("Room", lblRoom = new JLabel("-")));
        pnlGuest.add(createField("Check-In Date", lblCheckIn = new JLabel("-")));
        pnlGuest.add(createField("Check-out Date", lblCheckOut = new JLabel("-")));
        pnlGuest.add(new JLabel("")); 
        pnlGuest.add(new JLabel("")); 
        
        centerPanel.add(addSectionHeader("Guest Information", pnlGuest));
        centerPanel.add(Box.createVerticalStrut(20));

        // === CARD 2: BILLING DETAILS ===
        JPanel pnlBill = createCardPanel();
        pnlBill.setLayout(new BorderLayout());
        
        // Header
        JPanel pnlHeader = new JPanel(new GridLayout(1, 5));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        String[] headers = {"Description", "Date", "Quantity", "Rate", "Amount"};
        for (String h : headers) {
            JLabel l = new JLabel(h, SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            l.setForeground(Color.GRAY);
            pnlHeader.add(l);
        }
        pnlBill.add(pnlHeader, BorderLayout.NORTH);
        
        // Data
        JPanel pnlData = new JPanel(new GridLayout(1, 5));
        pnlData.setBackground(Color.WHITE);
        pnlData.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        lblDesc = new JLabel("-", SwingConstants.CENTER);
        lblDate = new JLabel("-", SwingConstants.CENTER);
        lblQty = new JLabel("-", SwingConstants.CENTER);
        lblRate = new JLabel("-", SwingConstants.CENTER);
        lblAmount = new JLabel("-", SwingConstants.CENTER);
        
        pnlData.add(lblDesc); pnlData.add(lblDate); pnlData.add(lblQty);
        pnlData.add(lblRate); pnlData.add(lblAmount);
        
        pnlBill.add(pnlData, BorderLayout.CENTER);
        
        // Total
        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlTotal.setBackground(Color.WHITE);
        JLabel lTotal = new JLabel("Total Amount");
        lTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lTotal.setForeground(new Color(40, 90, 160));
        
        lblTotal = new JLabel("RM 0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(new Color(40, 55, 80));
        
        pnlTotal.add(lTotal);
        pnlTotal.add(lblTotal);
        pnlBill.add(pnlTotal, BorderLayout.SOUTH);

        centerPanel.add(addSectionHeader("Billing Details", pnlBill));

        add(centerPanel, BorderLayout.CENTER);

        //BUTTONS 
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlButtons.setBackground(new Color(245, 248, 250));
        
        JButton btnPrint = new JButton("Print Receipt (PDF)");
        styleButton(btnPrint, new Color(40, 55, 80)); 
        btnPrint.addActionListener(e -> printReceipt());
        
        JButton btnProcess = new JButton("Confirm Payment"); // Renamed button
        styleButton(btnProcess, new Color(40, 167, 69)); 
        btnProcess.addActionListener(e -> processPayment());

        JButton btnCancel = new JButton("Cancel");
        styleButton(btnCancel, new Color(220, 53, 69)); 
        btnCancel.addActionListener(e -> cancelBooking());

        pnlButtons.add(btnPrint);
        pnlButtons.add(btnProcess);
        pnlButtons.add(btnCancel);

        add(pnlButtons, BorderLayout.SOUTH);
    }

    //LOGIC

    public void updateBillingDetails() {
        if (MainFrame.currentCustomer == null || MainFrame.selectedRoomNumber.isEmpty()) return;

        lblTitle.setText("Payment - Room " + MainFrame.selectedRoomNumber);
        lblGuestName.setText(MainFrame.currentCustomer.getName());
        lblRoom.setText(MainFrame.selectedRoomNumber + " - " + MainFrame.selectedRoomType);
        lblCheckIn.setText(MainFrame.currentCustomer.getCheckInDate());
        lblCheckOut.setText(MainFrame.currentCustomer.getCheckOutDate());

        LocalDate in = LocalDate.parse(MainFrame.currentCustomer.getCheckInDate());
        LocalDate out = LocalDate.parse(MainFrame.currentCustomer.getCheckOutDate());
        long nights = ChronoUnit.DAYS.between(in, out);
        if (nights <= 0) nights = 1;

        double rate = MainFrame.selectedRoomPrice > 0 ? MainFrame.selectedRoomPrice : 150.0;
        double total = nights * rate;

        lblDesc.setText("Room Rate (" + MainFrame.selectedRoomType + ")");
        lblDate.setText(MainFrame.currentCustomer.getCheckInDate());
        lblQty.setText(nights + " days");
        lblRate.setText("RM " + String.format("%.2f", rate));
        lblAmount.setText("RM " + String.format("%.2f", total));
        lblTotal.setText("RM " + String.format("%.2f", total));
    }

    private void processPayment() {
        String amountText = lblTotal.getText().replace("RM ", "").trim();
        double amount = Double.parseDouble(amountText);
        
        PaymentController pc = new PaymentController();
        boolean success = pc.processPayment(MainFrame.currentCustomer.getIC(), amount, "Credit Card");

        if (success) {
            JOptionPane.showMessageDialog(this, "Payment Successful! Room " + MainFrame.selectedRoomNumber + " is now Reserved.");
            
            
            updateRoomStatus(MainFrame.selectedRoomNumber, "Occupied");
            
            mainFrame.showScreen("RoomStatus");
        } else {
            JOptionPane.showMessageDialog(this, "Payment Failed.");
        }
    }
    
    private void cancelBooking() {
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this payment transaction?", "Cancel", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            
            updateRoomStatus(MainFrame.selectedRoomNumber, "Available"); 
            mainFrame.showScreen("RoomStatus");
        }
    }

    private void updateRoomStatus(String roomId, String status) {
        String sql = "UPDATE room SET status = ? WHERE room_id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setString(2, roomId);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void printReceipt() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Hotel Receipt");

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.scale(0.8, 0.8);
                centerPanel.printAll(graphics);
                return Printable.PAGE_EXISTS;
            }
        });

        if (job.printDialog()) {
            try { job.print(); } catch (PrinterException ex) { ex.printStackTrace(); }
        }
    }

    //UI HELPERS 
    private JPanel addSectionHeader(String title, JPanel content) {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(new Color(245, 248, 250));
        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(new Color(100, 110, 130));
        p.add(l, BorderLayout.NORTH);
        p.add(content, BorderLayout.CENTER);
        return p;
    }

    private JPanel createCardPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return p;
    }

    private JPanel createField(String title, JLabel valueLabel) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(Color.WHITE);
        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(Color.GRAY);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueLabel.setForeground(Color.BLACK);
        p.add(l, BorderLayout.NORTH);
        p.add(valueLabel, BorderLayout.CENTER);
        return p;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
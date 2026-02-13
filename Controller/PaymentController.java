package Controller;

import java.sql.*;
import Database.Database;

public class PaymentController {
    public boolean processPayment(String icNumber, double amount, String method) {
        String sql = "INSERT INTO payment (ic_number, amount, method, payment_date) VALUES (?, ?, ?, NOW())";
        
        try (Connection con = Database.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, icNumber);
            pst.setDouble(2, amount);
            pst.setString(3, method);
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
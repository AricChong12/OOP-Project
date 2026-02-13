
package Controller;

import java.sql.*;
import Model.Customer;

public class BookingController {
    private final String url = "jdbc:mysql://localhost:3306/hotel_management_system";

    public boolean createBooking(Customer customer, String roomNumber, String dateIn, String dateOut) {
        String insertBooking = "INSERT INTO booking (ic_number, room_id, check_in, check_out, status) VALUES (?, ?, ?, ?, 'Active')";
        String updateRoom = "UPDATE room SET status = 'Occupied' WHERE room_id = ?";
        
        try (Connection con = DriverManager.getConnection(url, "root", "")) {
            con.setAutoCommit(false); // Start Transaction

            try (PreparedStatement pstB = con.prepareStatement(insertBooking);
                 PreparedStatement pstR = con.prepareStatement(updateRoom)) {
                
                // 1. Prepare Booking
                pstB.setString(1, customer.getIC());
                pstB.setString(2, roomNumber);
                pstB.setString(3, dateIn);
                pstB.setString(4, dateOut);
                pstB.executeUpdate();
                
                // 2. Update Room Status
                pstR.setString(1, roomNumber);
                pstR.executeUpdate();
                
                con.commit(); // Save both
                return true;
            } catch (SQLException e) {
                con.rollback(); // Undo if error occurs
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}























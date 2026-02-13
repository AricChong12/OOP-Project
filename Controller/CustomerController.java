package Controller;

import java.sql.*;
import Model.Customer;

public class CustomerController {
    private final String url = "jdbc:mysql://localhost:3306/hotel_management_system";

    public boolean registerCustomer(Customer customer) {
        String sql = "INSERT INTO customer (name, ic_number, phone, email, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(url, "root", "");
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, customer.getName());
            pst.setString(2, customer.getIC());
            pst.setString(3, customer.getPhoneNo());
            pst.setString(4, customer.getEmail());
            pst.setString(5, customer.getPassword());
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Customer loginCustomer(String icNumber, String password) {
        String sql = "SELECT * FROM customer WHERE ic_number = ? AND password = ?";
        try (Connection con = DriverManager.getConnection(url, "root", "");
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, icNumber);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("ic_number"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
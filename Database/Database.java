package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import View.MainFrame;

public class Database {
    
    
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_management_system";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    
    public static Connection getConnection() throws SQLException {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver not found!", e);
        }
    }

   
    public static void main(String args[]) {
        
        try (Connection con = getConnection()) {
            if (con != null) {
                System.out.println("Database Connection Successful!");
                
                
                SwingUtilities.invokeLater(() -> {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                });
            }
        } catch (SQLException e) {
            System.err.println("Database Connection Failed! Ensure MySQL is running in XAMPP.");
            
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Could not connect to the database. Please start MySQL in XAMPP.", 
                "Database Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            
            e.printStackTrace();
        }
    }
}
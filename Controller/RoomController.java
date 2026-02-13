package Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Room;

public class RoomController {
    private final String url = "jdbc:mysql://localhost:3306/hotel_management_system";

    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM room";
        
        try (Connection con = DriverManager.getConnection(url, "root", "");
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Room room = new Room();
                room.setRoomID(rs.getInt("room_id"));
                room.setType(rs.getString("type"));
                
                room.setStatus(rs.getString("status")); 
                
                
                
                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
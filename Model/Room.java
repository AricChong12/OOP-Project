package Model;

public class Room {
    private int roomID;
    private String type;
    private double price;
    private String status;

    
    public Room() {
    }

    
    public Room(int roomID, String type, double price, String status) {
        this.roomID = roomID;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) { 
        this.roomID = roomID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) { 
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) { 
        this.price = price;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
package Model;

public class Booking {
    private int bookingID;
    private String icNumber;
    private int roomID;
    private String checkInDate;
    private String checkOutDate;
    private String status;

    public Booking(int bookingID, String icNumber, int roomID, String checkInDate, String checkOutDate, String status) {
        this.bookingID = bookingID;
        this.icNumber = icNumber;
        this.roomID = roomID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    
    public int getBookingID() { 
    	return bookingID; 
    }
    
    
    public String getIcNumber() { 
    	return icNumber; 
    }
    
    public int getRoomID() { 
    	return roomID; 
    }
    
    public String getCheckInDate() { 
    	return checkInDate; 
    }
    
    public String getCheckOutDate() { 
    	return checkOutDate; 
    }
    
    public String getStatus() { 
    	return status; 
    }
}
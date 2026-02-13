package Model;

public class Customer {
    
    private int customerID;
    private String name;
    private String ic;
    private String phoneNo;
    private String email;
    private String password;
    
    
    private String checkInDate; 
    private String checkOutDate;

    
    public Customer(int customerID, String name, String ic, String phoneNo, String email, String password) {
        this.customerID = customerID;
        this.name = name;
        this.ic = ic;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
    }

    
    public Customer(String name, String ic, String phoneNo, String email, String password) {
        this.name = name;
        this.ic = ic;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
    }

    
    public int getCustomerID() { 
    	return customerID; 
    }
    public String getName() { 
    	return name; 
    }
    public String getIC() { 
    	return ic; 
    }
    
    public String getPhoneNo() { 
    	return phoneNo; 
    }
    public String getEmail() { 
    	return email; 
    }
    
    public String getPassword() { 
    	return password; 
    }
    
    public String getCheckInDate() { 
    	return checkInDate; 
    }
    public void setCheckInDate(String date) { 
    	this.checkInDate = date; 
    }
    
    public String getCheckOutDate() { 
    	return checkOutDate; 
    }
    public void setCheckOutDate(String date) { 
    	this.checkOutDate = date;
    }
}
package Model;

import java.sql.Timestamp;

public class Payment {
    private int paymentID;
    private String icNumber;
    private double amount;
    private String method;
    private Timestamp paymentDate;

    public Payment(String icNumber, double amount, String method) {
        this.icNumber = icNumber;
        this.amount = amount;
        this.method = method;
    }

    // Getters
    public int getPaymentID() { 
    	return paymentID; 
    }
    public String getIcNumber() { 
    	return icNumber; 
    }
    
    public double getAmount() { 
    	return amount; 
    }
    public String getMethod() { 
    	return method; 
    }
    public Timestamp getPaymentDate() { 
    	return paymentDate; 
    }
}
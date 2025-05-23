package model;

import java.io.Serializable;
import java.sql.Date;

public class Shipment implements Serializable {
    private int shipmentID;
    private int orderID;
    private int customerID;
    private String shipmentMethod;
    private Date shipmentDate;
    private String streetAddress;
    private String city;
    private String state;
    private String postcode;
    private String status;
    private String trackingNumber;
    private Date createdDate;
    private Date updatedDate;
    private boolean finalized;
    
    public Shipment() {
    }
    
    public Shipment(int orderID, int customerID, String shipmentMethod, 
                   String streetAddress, String city, String state, String postcode) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.shipmentMethod = shipmentMethod;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postcode = postcode;
        this.status = "Pending";
        this.finalized = false;
    }
    
    public Shipment(int shipmentID, int orderID, int customerID, String shipmentMethod, 
                   Date shipmentDate, String streetAddress, String city, String state, 
                   String postcode, String status, String trackingNumber, 
                   Date createdDate, Date updatedDate, boolean finalized) {
        this.shipmentID = shipmentID;
        this.orderID = orderID;
        this.customerID = customerID;
        this.shipmentMethod = shipmentMethod;
        this.shipmentDate = shipmentDate;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postcode = postcode;
        this.status = status;
        this.trackingNumber = trackingNumber;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.finalized = finalized;
    }
    
    public int getShipmentID() {
        return shipmentID;
    }
    
    public void setShipmentID(int shipmentID) {
        this.shipmentID = shipmentID;
    }
    
    public int getOrderID() {
        return orderID;
    }
    
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    
    public int getCustomerID() {
        return customerID;
    }
    
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    
    public String getShipmentMethod() {
        return shipmentMethod;
    }
    
    public void setShipmentMethod(String shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
    }
    
    public Date getShipmentDate() {
        return shipmentDate;
    }
    
    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }
    
    public String getStreetAddress() {
        return streetAddress;
    }
    
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getPostcode() {
        return postcode;
    }
    
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getTrackingNumber() {
        return trackingNumber;
    }
    
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Date getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public boolean isFinalized() {
        return finalized;
    }
    
    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }
    
    public String getFullAddress() {
        return streetAddress + ", " + city + ", " + state + " " + postcode;
    }
    
    public String getShortDetails() {
        return "Shipment #" + shipmentID + " - " + status + 
               (trackingNumber != null ? " (Tracking: " + trackingNumber + ")" : "");
    }
    
    @Override
    public String toString() {
        return "Shipment{" +
                "shipmentID=" + shipmentID +
                ", orderID=" + orderID +
                ", customerID=" + customerID +
                ", shipmentMethod='" + shipmentMethod + '\'' +
                ", status='" + status + '\'' +
                ", finalized=" + finalized +
                '}';
    }
}
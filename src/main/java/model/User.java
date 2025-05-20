package model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String streetAddress;
    private String country;
    private String state;
    private String postcode;
    private String suburb;
    private String role;
    
    public User() {
    }
    
    public User(String firstName, String lastName, String email, String password, String phone,
                String streetAddress, String country, String state, String postcode, String suburb, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.streetAddress = streetAddress;
        this.country = country;
        this.state = state;
        this.postcode = postcode;
        this.suburb = suburb;
        this.role = role;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getStreetAddress() {
        return streetAddress;
    }
    
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
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
    
    public String getSuburb() {
        return suburb;
    }
    
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    /**
     * Checks if the user has an admin role.
     * @return true if the user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
    
    /**
     * Checks if the user has a staff role.
     * @return true if the user is a staff member, false otherwise
     */
    public boolean isStaff() {
        return "staff".equalsIgnoreCase(role);
    }
    
    /**
     * Checks if the user has a customer role.
     * @return true if the user is a customer, false otherwise
     */
    public boolean isCustomer() {
        return "customer".equalsIgnoreCase(role);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
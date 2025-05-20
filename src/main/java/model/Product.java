package model;

import java.io.Serializable;

/**
 * Product class representing a product in the system.
 * This class implements Serializable to allow session storage.
 */
public class Product implements Serializable {
    private int productID;
    private String name;
    private String imageUrl;
    private String description;
    private double price;
    private int quantity;
    private boolean favourited;

    /**
     * Default constructor
     */
    public Product() {}

    /**
     * Constructor with all product details
     * 
     * @param productID The product ID
     * @param name The product name
     * @param imageUrl The product image URL
     * @param description The product description
     * @param price The product price
     * @param quantity The product quantity
     * @param favourited Whether the product is favourited
     */
    public Product(int productID, String name, String imageUrl, String description, double price, int quantity,
            boolean favourited) {
        this.productID = productID;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.favourited = favourited;
    }
    
    // Getters & Setters
    
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isFavourited() {
        return favourited;
    }

    public void setFavourited(boolean favourited) {
        this.favourited = favourited;
    }
    
    /**
     * Formats the price as a string with 2 decimal places
     * 
     * @return Formatted price string
     */
    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }
    
    /**
     * Checks if the product is in stock
     * 
     * @return true if quantity > 0, false otherwise
     */
    public boolean isInStock() {
        return quantity > 0;
    }
    
    /**
     * Gets a short description (truncated if needed)
     * 
     * @param maxLength Maximum length of the short description
     * @return Truncated description
     */
    public String getShortDescription(int maxLength) {
        if (description == null) {
            return "";
        }
        
        if (description.length() <= maxLength) {
            return description;
        }
        
        return description.substring(0, maxLength) + "...";
    }
}
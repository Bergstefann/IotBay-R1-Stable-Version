package model;

import java.io.Serializable;

public class Product implements Serializable {
    private int productID;
    private String name;
    private String imageUrl;
    private String description;
    private double price;
    private int quantity;
    private boolean favourited;

    public Product() {}

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
    
    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }
    
    public boolean isInStock() {
        return quantity > 0;
    }
    
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
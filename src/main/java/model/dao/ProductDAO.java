package model.dao;

import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO extends BaseDAO {
    
    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    public ProductDAO(Connection conn) throws SQLException {
        super(conn);
    }
    
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        int productId = rs.getInt("productID");
        String name = rs.getString("name");
        String imageUrl = rs.getString("imageUrl");
        String description = rs.getString("description");
        double price = rs.getDouble("price");
        int quantity = rs.getInt("quantity");
        boolean favourited = rs.getBoolean("favourited");
        
        return new Product(productId, name, imageUrl, description, price, quantity, favourited);
    }
    
    public List<Product> getAllProducts() throws SQLException {
        String query = "SELECT * FROM Product";
        ResultSet rs = st.executeQuery(query);
        
        List<Product> products = new ArrayList<>();
        
        while (rs.next()) {
            Product product = extractProductFromResultSet(rs);
            products.add(product);
        }
        
        return products;
    }
    
    public Product getProductById(int productId) throws SQLException {
        String query = "SELECT * FROM Product WHERE productID = " + productId;
        ResultSet rs = st.executeQuery(query);
        
        if (rs.next()) {
            return extractProductFromResultSet(rs);
        }
        
        return null;
    }
    
    public List<Product> searchProductsByName(String name) throws SQLException {
        String safeName = sanitize(name);
        String query = "SELECT * FROM Product WHERE name LIKE '%" + safeName + "%'";
        ResultSet rs = st.executeQuery(query);
        
        List<Product> products = new ArrayList<>();
        
        while (rs.next()) {
            Product product = extractProductFromResultSet(rs);
            products.add(product);
        }
        
        return products;
    }
    
    public boolean addProduct(String name, String imageUrl, String description, double price, int quantity) throws SQLException {
        String safeName = sanitize(name);
        String safeImageUrl = sanitize(imageUrl);
        String safeDescription = sanitize(description);
        
        String query = "INSERT INTO Product (name, imageUrl, description, price, quantity) VALUES ('"
                + safeName + "', '" + safeImageUrl + "', '" + safeDescription + "', " 
                + price + ", " + quantity + ")";
        
        return executeUpdate(query);
    }
    
    public boolean updateProduct(int productId, String name, String imageUrl, String description, double price, int quantity) throws SQLException {
        String safeName = sanitize(name);
        String safeImageUrl = sanitize(imageUrl);
        String safeDescription = sanitize(description);
        
        String query = "UPDATE Product SET name = '" + safeName + 
                "', imageUrl = '" + safeImageUrl + 
                "', description = '" + safeDescription + 
                "', price = " + price + 
                ", quantity = " + quantity + 
                " WHERE productID = " + productId;
        
        return executeUpdate(query);
    }
    
    public boolean updateProductQuantity(int productId, int newQuantity) throws SQLException {
        if (newQuantity < 0) {
            return false;
        }
        
        String query = "UPDATE Product SET quantity = " + newQuantity + 
                " WHERE productID = " + productId;
        
        return executeUpdate(query);
    }
    
    public boolean deleteProduct(int productId) throws SQLException {
        String query = "DELETE FROM Product WHERE productID = " + productId;
        return executeUpdate(query);
    }
    
    public boolean toggleProductFavorite(int productId, boolean favorited) throws SQLException {
        int favValue = favorited ? 1 : 0;
        String query = "UPDATE Product SET favourited = " + favValue + 
                " WHERE productID = " + productId;
        
        return executeUpdate(query);
    }
    
    public List<Product> getFeaturedProducts(int limit) throws SQLException {
        String query = "SELECT * FROM Product ORDER BY productID DESC LIMIT " + limit;
        ResultSet rs = st.executeQuery(query);
        
        List<Product> products = new ArrayList<>();
        
        while (rs.next()) {
            Product product = extractProductFromResultSet(rs);
            products.add(product);
        }
        
        return products;
    }
}
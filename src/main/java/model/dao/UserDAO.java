package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.User;

public class UserDAO {
    private Connection conn;
    private DBConnector db;
    
    public UserDAO(Connection conn) throws SQLException {
        if (conn == null) {
            throw new SQLException("Database connection is null");
        }
        this.conn = conn;
        
        try (Statement stmt = getValidConnection().createStatement()) {
            ResultSet tables = getValidConnection().getMetaData().getTables(null, null, "User", null);
            if (!tables.next()) {
                System.out.println("User table not found. Creating it...");
                createUserTable();
            } else {
                System.out.println("User table exists");
            }
        }
    }
    
    private Connection getValidConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("Connection is closed or null in UserDAO. Attempting to reconnect.");
                if (db == null) {
                    try {
                        db = new DBConnector();
                        conn = db.getValidConnection();
                    } catch (ClassNotFoundException | SQLException e) {
                        System.err.println("Error creating new connection: " + e.getMessage());
                    }
                } else {
                    conn = db.getValidConnection();
                }
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("Error validating connection in UserDAO: " + e.getMessage());
            return conn; 
        }
    }
    
    private void createUserTable() throws SQLException {
        String createUserTable = 
            "CREATE TABLE IF NOT EXISTS User (" +
            "userID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "firstName VARCHAR(20)," +
            "lastName VARCHAR(20)," +
            "email VARCHAR(50) UNIQUE," +
            "password VARCHAR(20)," +
            "phoneNumber VARCHAR(15)," +
            "streetAddress VARCHAR(20)," +
            "country VARCHAR(20)," +
            "state VARCHAR(10)," +
            "postcode VARCHAR(4)," +
            "suburb VARCHAR(10)," +
            "createdDate DATE DEFAULT CURRENT_TIMESTAMP," +
            "lastUpdated DATE DEFAULT CURRENT_TIMESTAMP," +
            "role VARCHAR(20)" +
            ")";
        
        try (Statement stmt = getValidConnection().createStatement()) {
            stmt.execute(createUserTable);
            System.out.println("User table created successfully");
        }
    }
    
    public User findUser(String email, String password) throws SQLException {
        String query = "SELECT * FROM User WHERE email = ? AND password = ?";
        System.out.println("Executing query: " + query + " for email: " + email);
        
        try (PreparedStatement stmt = getValidConnection().prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("userID"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setLastName(rs.getString("lastName"));
                    user.setPhone(rs.getString("phoneNumber"));
                    
                    try {
                        user.setStreetAddress(rs.getString("streetAddress"));
                        user.setCountry(rs.getString("country"));
                        user.setState(rs.getString("state"));
                        user.setPostcode(rs.getString("postcode"));
                        user.setSuburb(rs.getString("suburb"));
                        user.setRole(rs.getString("role"));
                    } catch (Exception e) {
                    }
                    
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in findUser: " + e.getMessage());
            throw e;
        }
        return null;
    }
    
    public boolean createUser(User user) throws SQLException {
        String query = "INSERT INTO User (firstName, lastName, email, password, phoneNumber, role) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = getValidConnection().prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getRole() != null ? user.getRole() : "customer");
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in createUser: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM User WHERE email = ?";
        
        try (PreparedStatement stmt = getValidConnection().prepareStatement(query)) {
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if email exists: " + e.getMessage());
            throw e;
        }
        return false;
    }
    
    public Connection getConnection() {
        return getValidConnection();
    }
}
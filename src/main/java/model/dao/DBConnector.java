package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class DBConnector extends DB {
    private static Connection sharedConnection = null;
    
    public DBConnector() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        
        File dbFile = new File(URL.replace("jdbc:sqlite:", ""));
        
        if (!dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }
        
        if (sharedConnection == null || sharedConnection.isClosed()) {
            sharedConnection = DriverManager.getConnection(URL);
            conn = sharedConnection;
            initializeDatabase();
        } else {
            conn = sharedConnection;
        }
    }
    
    @Override
    public Connection openConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                if (sharedConnection == null || sharedConnection.isClosed()) {
                    sharedConnection = DriverManager.getConnection(URL);
                }
                conn = sharedConnection;
            }
            return conn;
        } catch (SQLException e) {
            return null;
        }
    }
    
    public Connection getValidConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                return openConnection();
            }
            return conn;
        } catch (SQLException e) {
            return openConnection();
        }
    }
    
    @Override
    public void closeConnection() throws SQLException {
    }
    
    private void initializeDatabase() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "email TEXT UNIQUE NOT NULL," +
                         "password TEXT NOT NULL," +
                         "first_name TEXT NOT NULL," +
                         "last_name TEXT NOT NULL," +
                         "phone TEXT," +
                         "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                         ")");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS User (" +
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
                         ")");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "name TEXT NOT NULL," +
                         "description TEXT," +
                         "price REAL NOT NULL," +
                         "stock INTEGER NOT NULL DEFAULT 0," +
                         "category TEXT," +
                         "image_url TEXT" +
                         ")");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS Product (" +
                         "productID INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "name VARCHAR(100) NOT NULL," +
                         "imageUrl VARCHAR(255)," +
                         "description TEXT," +
                         "price DECIMAL(10,2) NOT NULL," +
                         "quantity INTEGER DEFAULT 0," +
                         "favourited BOOLEAN DEFAULT 0" +
                         ")");
            
        } catch (SQLException e) {
        }
    }
    
    public Connection getConnection() {
        return getValidConnection();
    }
}
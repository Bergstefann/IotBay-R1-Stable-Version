package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

/**
 * Database connector class responsible for establishing and managing the connection to the SQLite database.
 */
public class DBConnector extends DB {
    // Static shared connection to persist between requests
    private static Connection sharedConnection = null;
    
    /**
     * Constructor for DBConnector. Initializes the database connection.
     *
     * @throws ClassNotFoundException if the SQLite JDBC driver is not found
     * @throws SQLException if there's an error connecting to the database
     */
    public DBConnector() throws ClassNotFoundException, SQLException {
        // Load the SQLite JDBC driver
        System.out.println("Loading JDBC driver: " + driver);
        Class.forName(driver);
        
        // Check if database file exists and is valid
        File dbFile = new File(URL.replace("jdbc:sqlite:", ""));
        System.out.println("Database path: " + dbFile.getAbsolutePath());
        
        if (!dbFile.getParentFile().exists()) {
            System.out.println("Creating parent directories for database file");
            dbFile.getParentFile().mkdirs();
        }
        
        // Use shared connection if available, otherwise create a new one
        if (sharedConnection == null || sharedConnection.isClosed()) {
            // Establish a connection to the database
            System.out.println("Creating new database connection");
            sharedConnection = DriverManager.getConnection(URL);
            conn = sharedConnection;
            
            // Initialize database tables
            initializeDatabase();
        } else {
            System.out.println("Reusing existing database connection");
            conn = sharedConnection;
        }
        
        System.out.println("Connection established: " + (conn != null));
    }
    
    /**
     * Opens a connection to the database.
     *
     * @return the database connection
     */
    @Override
    public Connection openConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("Re-establishing database connection");
                if (sharedConnection == null || sharedConnection.isClosed()) {
                    sharedConnection = DriverManager.getConnection(URL);
                }
                conn = sharedConnection;
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("Error opening database connection: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Checks if the connection is valid and returns a valid connection.
     * 
     * @return a valid database connection
     */
    public Connection getValidConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                return openConnection();
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("Error checking connection: " + e.getMessage());
            return openConnection();
        }
    }
    
    /**
     * Closes the database connection.
     * Now modified to keep the connection alive between requests.
     *
     * @throws SQLException if there's an error closing the connection
     */
    @Override
    public void closeConnection() throws SQLException {
        // Intentionally not closing the connection to keep it available for future requests
        System.out.println("Connection kept alive for future requests");
        
        // Uncomment below to actually close the connection when application shuts down
        // We'll handle this in a shutdown hook or context listener instead
        /*
        if (conn != null && !conn.isClosed()) {
            conn.close();
            System.out.println("Database connection closed");
        }
        */
    }
    
    /**
     * Initializes the database tables if they don't exist.
     */
    private void initializeDatabase() {
        try (Statement stmt = conn.createStatement()) {
            // Create users table if it doesn't exist
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "email TEXT UNIQUE NOT NULL," +
                         "password TEXT NOT NULL," +
                         "first_name TEXT NOT NULL," +
                         "last_name TEXT NOT NULL," +
                         "phone TEXT," +
                         "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                         ")");
            
            // Create User table with your project's schema
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
            
            // Create products table if it doesn't exist
            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "name TEXT NOT NULL," +
                         "description TEXT," +
                         "price REAL NOT NULL," +
                         "stock INTEGER NOT NULL DEFAULT 0," +
                         "category TEXT," +
                         "image_url TEXT" +
                         ")");
            
            // Create Product table with your project's schema
            stmt.execute("CREATE TABLE IF NOT EXISTS Product (" +
                         "productID INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "name VARCHAR(100) NOT NULL," +
                         "imageUrl VARCHAR(255)," +
                         "description TEXT," +
                         "price DECIMAL(10,2) NOT NULL," +
                         "quantity INTEGER DEFAULT 0," +
                         "favourited BOOLEAN DEFAULT 0" +
                         ")");
            
            // Add other tables as needed for your application
            
            System.out.println("Database tables initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Provides access to the connection for DAOs
     * 
     * @return the current database connection
     */
    public Connection getConnection() {
        return getValidConnection();
    }
}
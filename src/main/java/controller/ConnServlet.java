package controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.dao.*;

@WebServlet(name = "ConnServlet", urlPatterns = {"/ConnServlet"})
public class ConnServlet extends HttpServlet {
    private DBConnector db;
    private UserDAO userManager;
    private ProductDAO productManager;
    private Connection conn;
   
    private static final Logger LOGGER = Logger.getLogger(ConnServlet.class.getName());

    @Override
    public void init() {
        System.out.println("ConnServlet initializing...");
        
        // Define database paths
        String dbScriptPath = "C:/Users/thoma/OneDrive/Documents/IotBay R1 Stable Version/src/main/database/iotbay.db";
        String newDbPath = "C:/Users/thoma/OneDrive/Documents/IotBay R1 Stable Version/src/main/database/iotbay_data.db";
        
        File dbScriptFile = new File(dbScriptPath);
        File newDbFile = new File(newDbPath);
        
        try {
            // Check if the script file exists
            System.out.println("Checking SQL script file at: " + dbScriptFile.getAbsolutePath());
            
            if (dbScriptFile.exists()) {
                System.out.println("SQL script file exists, size: " + dbScriptFile.length() + " bytes");
                
                // Read the SQL script content
                StringBuilder scriptContent = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(dbScriptFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        scriptContent.append(line).append("\n");
                    }
                }
                
                // Delete existing database file if it exists
                if (newDbFile.exists()) {
                    System.out.println("Existing database file found. Deleting it to create a fresh one.");
                    newDbFile.delete();
                }
                
                // Create parent directories if needed
                if (!newDbFile.getParentFile().exists()) {
                    newDbFile.getParentFile().mkdirs();
                }
                
                // Create and initialize the new database
                Class.forName("org.sqlite.JDBC");
                try (Connection initConn = DriverManager.getConnection("jdbc:sqlite:" + newDbPath);
                     Statement stmt = initConn.createStatement()) {
                    
                    System.out.println("Creating new SQLite database: " + newDbPath);
                    
                    // Execute the script content
                    // Split by semicolons to execute each statement separately
                    String[] statements = scriptContent.toString().split(";");
                    for (String statement : statements) {
                        String trimmedStatement = statement.trim();
                        if (!trimmedStatement.isEmpty() && !trimmedStatement.startsWith("--")) {
                            try {
                                stmt.execute(trimmedStatement);
                                System.out.println("Executed SQL: " + trimmedStatement.substring(0, 
                                    Math.min(50, trimmedStatement.length())) + 
                                    (trimmedStatement.length() > 50 ? "..." : ""));
                            } catch (SQLException e) {
                                System.err.println("Error executing statement: " + trimmedStatement);
                                System.err.println("Error message: " + e.getMessage());
                                // Continue with other statements
                            }
                        }
                    }
                    
                    System.out.println("Database created and initialized successfully");
                }
                
                // Update the system property to use the new database
                System.setProperty("database.path", newDbPath);
                
            } else {
                System.out.println("SQL script file does not exist at: " + dbScriptFile.getAbsolutePath());
                // Check if the database already exists
                if (newDbFile.exists()) {
                    System.out.println("Using existing database file: " + newDbFile.getAbsolutePath());
                    System.setProperty("database.path", newDbPath);
                } else {
                    System.out.println("Neither SQL script nor database file exists. Using default database path.");
                }
            }
            
            // Initialize DB connector with the new database
            db = new DBConnector();
            conn = db.openConnection();
            
            // Initialize DAOs
            try {
                userManager = new UserDAO(conn);
                // Only uncomment this if you've created ProductDAO
                productManager = new ProductDAO(conn);
                
                // Store DAOs in servlet context for application-wide access
                getServletContext().setAttribute("userManager", userManager);
                getServletContext().setAttribute("productManager", productManager);
                
                System.out.println("Database connection established successfully");
            } catch (SQLException ex) {
                System.out.println("Error initializing DAOs: " + ex.getMessage());
                ex.printStackTrace();
            }
            
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            System.out.println("ERROR initializing database: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        
        try {
            // Check if db was initialized properly
            if (db == null) {
                throw new ServletException("Database connection not initialized");
            }
            
            conn = db.openConnection();
            if (conn == null) {
                throw new ServletException("Failed to open database connection");
            }
            
            // Make sure DAOs are initialized and stored in session
            if (userManager == null) {
                try {
                    userManager = new UserDAO(conn);
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error creating UserDAO", ex);
                    throw new ServletException("Error creating UserDAO", ex);
                }
            }
            
            if (productManager == null) {
                try {
                    productManager = new ProductDAO(conn);
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error creating ProductDAO", ex);
                    throw new ServletException("Error creating ProductDAO", ex);
                }
            }
            
            session.setAttribute("manager", userManager);
            session.setAttribute("productManager", productManager);
            
            // Respond with success indicator for debugging
            response.getWriter().println("Database connection successful");
            
        } catch (ServletException ex) {
            LOGGER.log(Level.SEVERE, "Database connection error", ex);
            
            // Send an error message to the client
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Database connection error: " + ex.getMessage());
            
            // Clear any incomplete objects from the session
            session.removeAttribute("manager");
            session.removeAttribute("productManager");
        }
    }
    
    @Override
    public void destroy() {
        try {
            if (db != null) {
                db.closeConnection();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error closing database connection", ex);
        }
    }
}
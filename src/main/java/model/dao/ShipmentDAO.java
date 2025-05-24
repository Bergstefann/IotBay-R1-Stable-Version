package model.dao;
import model.Shipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Shipment objects
 * Handles all database operations for shipment management
 * Compatible with existing database schema
 */
public class ShipmentDAO extends BaseDAO {
   
    private static final Logger LOGGER = Logger.getLogger(ShipmentDAO.class.getName());
    
    /**
     * Constructor - ensures table exists
     * @param conn Active database connection object
     * @throws SQLException if database fails
     */
    public ShipmentDAO(Connection conn) throws SQLException {
        super(conn);
       
        // Check if table needs to be updated with missing columns
        updateShipmentTableStructure();
       
        // Populate with sample data if table is empty
        if (getShipmentCount() == 0) {
            insertSampleData();
        }
    }
   
    private void updateShipmentTableStructure() {
        try {
            // Check if orderID column exists
            boolean hasOrderID = false;
            boolean hasCustomerID = false;
            boolean hasShipmentMethod = false;
            boolean hasFinalized = false;
            
            ResultSet columns = conn.getMetaData().getColumns(null, null, "Shipment", null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                if ("orderID".equalsIgnoreCase(columnName)) hasOrderID = true;
                if ("customerID".equalsIgnoreCase(columnName)) hasCustomerID = true;
                if ("shipmentMethod".equalsIgnoreCase(columnName)) hasShipmentMethod = true;
                if ("finalized".equalsIgnoreCase(columnName)) hasFinalized = true;
            }
            
            // Add missing columns
            if (!hasOrderID) {
                st.execute("ALTER TABLE Shipment ADD COLUMN orderID INTEGER NOT NULL DEFAULT 1");
                LOGGER.log(Level.INFO, "Added orderID column to Shipment table");
            }
            
            if (!hasCustomerID) {
                st.execute("ALTER TABLE Shipment ADD COLUMN customerID INTEGER NOT NULL DEFAULT 1");
                LOGGER.log(Level.INFO, "Added customerID column to Shipment table");
            }
            
            if (!hasShipmentMethod) {
                st.execute("ALTER TABLE Shipment ADD COLUMN shipmentMethod VARCHAR(50)");
                LOGGER.log(Level.INFO, "Added shipmentMethod column to Shipment table");
            }
            
            if (!hasFinalized) {
                st.execute("ALTER TABLE Shipment ADD COLUMN finalized BOOLEAN DEFAULT 0");
                LOGGER.log(Level.INFO, "Added finalized column to Shipment table");
            }
            
            boolean hasShipmentDate = false;
            columns = conn.getMetaData().getColumns(null, null, "Shipment", null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                if ("shipmentDate".equalsIgnoreCase(columnName)) hasShipmentDate = true;
            }
            
            if (!hasShipmentDate) {
                st.execute("ALTER TABLE Shipment ADD COLUMN shipmentDate DATE");
                LOGGER.log(Level.INFO, "Added shipmentDate column to Shipment table");
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating Shipment table structure", e);
        }
    }
   
    /**
     * Counts shipments to determine if sample data is needed
     * @return Number of shipments
     */
    private int getShipmentCount() {
        try {
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Shipment");
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting shipments", e);
        }
        return 0;
    }
   
    /**
     * Inserts sample shipments using existing User and Order data
     */
    private void insertSampleData() {
        try {
            // Get existing customer IDs from User table
            List<Integer> userIDs = new ArrayList<>();
            try {
                ResultSet rs = st.executeQuery("SELECT userID FROM User WHERE role = 'Customer' OR role IS NULL LIMIT 5");
                while (rs.next()) {
                    userIDs.add(rs.getInt("userID"));
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Could not fetch users, using default", e);
            }
            
            // Get existing order IDs
            List<Integer> orderIDs = new ArrayList<>();
            try {
                ResultSet rs = st.executeQuery("SELECT orderID FROM \"Order\" LIMIT 5");
                while (rs.next()) {
                    orderIDs.add(rs.getInt("orderID"));
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Could not fetch orders, using default", e);
            }
            
            // Use defaults if no data found
            if (userIDs.isEmpty()) userIDs.add(1);
            if (orderIDs.isEmpty()) orderIDs.add(1);
           
            // Sample data arrays
            String[] shipmentMethods = {"Standard", "Express", "Priority", "Overnight", "Economy"};
            String[] statuses = {"Pending", "Finalized"};
            String[] cities = {"Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide"};
            String[] states = {"NSW", "VIC", "QLD", "WA", "SA"};
            String[] postcodes = {"2000", "3000", "4000", "6000", "5000"};
           
            // Insert sample shipments
            for (int i = 0; i < 8; i++) {
                int userIndex = i % userIDs.size();
                int orderIndex = i % orderIDs.size();
                int methodIndex = i % shipmentMethods.length;
                int statusIndex = i % statuses.length;
                int locationIndex = i % cities.length;
                boolean finalized = statusIndex > 1;
               
                String insertSQL = "INSERT INTO Shipment (orderID, customerID, shipmentMethod, " +
                                  "streetAddress, city, state, postcode, status, trackingNumber, finalized) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
               
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setInt(1, orderIDs.get(orderIndex));
                    pstmt.setInt(2, userIDs.get(userIndex));
                    pstmt.setString(3, shipmentMethods[methodIndex]);
                    pstmt.setString(4, (123 + i) + " Sample Street");
                    pstmt.setString(5, cities[locationIndex]);
                    pstmt.setString(6, states[locationIndex]);
                    pstmt.setString(7, postcodes[locationIndex]);
                    pstmt.setString(8, statuses[statusIndex]);
                    pstmt.setString(9, finalized ? "TRK" + (100000 + i) : null);
                    pstmt.setBoolean(10, finalized);
                   
                    pstmt.executeUpdate();
                }
            }
           
            LOGGER.log(Level.INFO, "Sample shipments inserted successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting sample shipments", e);
        }
    }

    /**
     * Extracts Shipment object from ResultSet
     */
    private Shipment extractShipmentFromResultSet(ResultSet rs) throws SQLException {
        Shipment shipment = new Shipment();
        shipment.setShipmentID(rs.getInt("shipmentID"));
        
        // Handle missing columns
        try {
            shipment.setOrderID(rs.getInt("orderID"));
        } catch (SQLException e) {
            shipment.setOrderID(1); // Default value
        }
        
        try {
            shipment.setCustomerID(rs.getInt("customerID"));
        } catch (SQLException e) {
            shipment.setCustomerID(1); // Default value
        }
        
        try {
            shipment.setShipmentMethod(rs.getString("shipmentMethod"));
        } catch (SQLException e) {
            shipment.setShipmentMethod("Standard"); // Default value
        }
        
        try {
            shipment.setShipmentDate(rs.getDate("shipmentDate"));
        } catch (SQLException e) {
        }
        
        shipment.setStreetAddress(rs.getString("streetAddress"));
        shipment.setCity(rs.getString("city"));
        shipment.setState(rs.getString("state"));
        shipment.setPostcode(rs.getString("postcode"));
        shipment.setStatus(rs.getString("status"));
        shipment.setTrackingNumber(rs.getString("trackingNumber"));
        shipment.setCreatedDate(rs.getDate("createdDate"));
        shipment.setUpdatedDate(rs.getDate("updatedDate"));
        
        try {
            shipment.setFinalized(rs.getBoolean("finalized"));
        } catch (SQLException e) {
            shipment.setFinalized(false); // Default value
        }
        
        return shipment;
    }

    public int createShipment(Shipment shipment) throws SQLException {
        String query = "INSERT INTO Shipment (orderID, customerID, shipmentMethod, shipmentDate, " +
                      "streetAddress, city, state, postcode, status, trackingNumber, finalized) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
       
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, shipment.getOrderID());
            pstmt.setInt(2, shipment.getCustomerID());
            pstmt.setString(3, shipment.getShipmentMethod());
            pstmt.setDate(4, shipment.getShipmentDate());
            pstmt.setString(5, shipment.getStreetAddress());
            pstmt.setString(6, shipment.getCity());
            pstmt.setString(7, shipment.getState());
            pstmt.setString(8, shipment.getPostcode());
            pstmt.setString(9, shipment.getStatus() != null ? shipment.getStatus() : "Pending");
            pstmt.setString(10, shipment.getTrackingNumber());
            pstmt.setBoolean(11, shipment.isFinalized());
           
            int affectedRows = pstmt.executeUpdate();
           
            if (affectedRows == 0) {
                throw new SQLException("Creating shipment failed, no rows affected.");
            }
           
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    shipment.setShipmentID(id);
                    return id;
                } else {
                    throw new SQLException("Creating shipment failed, no ID obtained.");
                }
            }
        }
    }

    public Shipment getShipmentById(int shipmentId) throws SQLException {
        String query = "SELECT * FROM Shipment WHERE shipmentID = ?";
       
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, shipmentId);
           
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractShipmentFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Shipment> getShipmentsByCustomerId(int customerId) throws SQLException {
        String query = "SELECT * FROM Shipment WHERE customerID = ? ORDER BY updatedDate DESC";
        List<Shipment> shipments = new ArrayList<>();
       
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
           
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    shipments.add(extractShipmentFromResultSet(rs));
                }
            }
        }
        return shipments;
    }

    public List<Shipment> getShipmentsByOrderId(int orderId) throws SQLException {
        String query = "SELECT * FROM Shipment WHERE orderID = ? ORDER BY updatedDate DESC";
        List<Shipment> shipments = new ArrayList<>();
       
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
           
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    shipments.add(extractShipmentFromResultSet(rs));
                }
            }
        }
        return shipments;
    }

    public List<Shipment> searchShipments(Integer customerId, Date startDate, Date endDate, Integer shipmentId) throws SQLException {
        List<Shipment> allShipments = getShipmentsByCustomerId(customerId);
        List<Shipment> filteredShipments = new ArrayList<>();
       
        for (Shipment shipment : allShipments) {
            boolean include = true;
           
            if (shipmentId != null && shipment.getShipmentID() != shipmentId) {
                include = false;
            }
           
            if (include && startDate != null && shipment.getCreatedDate() != null) {
                if (shipment.getCreatedDate().before(startDate)) {
                    include = false;
                }
            }
           
            if (include && endDate != null && shipment.getCreatedDate() != null) {
                if (shipment.getCreatedDate().after(endDate)) {
                    include = false;
                }
            }
           
            if (include) {
                filteredShipments.add(shipment);
            }
        }
        return filteredShipments;
    }

    public boolean updateShipment(Shipment shipment) throws SQLException {
        String query = "UPDATE Shipment SET orderID = ?, customerID = ?, shipmentMethod = ?, " +
                      "shipmentDate = ?, streetAddress = ?, city = ?, state = ?, postcode = ?, " +
                      "status = ?, trackingNumber = ?, finalized = ?, updatedDate = CURRENT_TIMESTAMP " +
                      "WHERE shipmentID = ?";
       
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, shipment.getOrderID());
            pstmt.setInt(2, shipment.getCustomerID());
            pstmt.setString(3, shipment.getShipmentMethod());
            pstmt.setDate(4, shipment.getShipmentDate());
            pstmt.setString(5, shipment.getStreetAddress());
            pstmt.setString(6, shipment.getCity());
            pstmt.setString(7, shipment.getState());
            pstmt.setString(8, shipment.getPostcode());
            pstmt.setString(9, shipment.getStatus());
            pstmt.setString(10, shipment.getTrackingNumber());
            pstmt.setBoolean(11, shipment.isFinalized());
            pstmt.setInt(12, shipment.getShipmentID());
           
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean finalizeShipment(int shipmentId, String trackingNumber) throws SQLException {
        String query = "UPDATE Shipment SET status = 'Processing', trackingNumber = ?, " +
                      "finalized = 1, updatedDate = CURRENT_TIMESTAMP " +
                      "WHERE shipmentID = ? AND (finalized = 0 OR finalized IS NULL)";
       
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, trackingNumber);
            pstmt.setInt(2, shipmentId);
           
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteShipment(int shipmentId) throws SQLException {
        String query = "DELETE FROM Shipment WHERE shipmentID = ?";
       
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, shipmentId);
           
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
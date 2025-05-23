package model.dao;

import model.Shipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShipmentDAO extends BaseDAO {
    
    private static final Logger LOGGER = Logger.getLogger(ShipmentDAO.class.getName());

    public ShipmentDAO(Connection conn) throws SQLException {
        super(conn);
        
        createShipmentTable();
        
        if (getShipmentCount() == 0) {
            insertSampleData();
        }
    }
    
    private void createShipmentTable() {
        try {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Shipment (" +
                "shipmentID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "orderID INTEGER NOT NULL, " +
                "customerID INTEGER NOT NULL, " +
                "shipmentMethod VARCHAR(50), " +
                "shipmentDate DATE, " +
                "streetAddress VARCHAR(100), " +
                "city VARCHAR(50), " +
                "state VARCHAR(20), " +
                "postcode VARCHAR(10), " +
                "status VARCHAR(20) DEFAULT 'Pending', " +
                "trackingNumber VARCHAR(50), " +
                "finalized BOOLEAN DEFAULT 0, " +
                "createdDate DATE DEFAULT CURRENT_TIMESTAMP, " +
                "updatedDate DATE DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (orderID) REFERENCES \"Order\"(orderID), " +
                "FOREIGN KEY (customerID) REFERENCES User(userID)" +
                ")";
            
            st.execute(createTableSQL);
            LOGGER.log(Level.INFO, "Shipment table created or already exists");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating Shipment table", e);
        }
    }
    
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
    
    private void insertSampleData() {
        try {
            List<Integer> userIDs = new ArrayList<>();
            ResultSet rs = st.executeQuery("SELECT userID FROM User WHERE role = 'Customer' LIMIT 5");
            while (rs.next()) {
                userIDs.add(rs.getInt("userID"));
            }
            
            if (userIDs.isEmpty()) {
                st.executeUpdate("INSERT INTO \"Order\" (customerID, orderStatus) VALUES (1, 'Pending')");
                userIDs.add(1);
            }
            
            List<Integer> orderIDs = new ArrayList<>();
            rs = st.executeQuery("SELECT orderID FROM \"Order\" LIMIT 5");
            while (rs.next()) {
                orderIDs.add(rs.getInt("orderID"));
            }
            
            if (orderIDs.isEmpty()) {
                st.executeUpdate("INSERT INTO \"Order\" (customerID, orderStatus) VALUES (1, 'Pending')");
                rs = st.executeQuery("SELECT last_insert_rowid() as orderID");
                if (rs.next()) {
                    orderIDs.add(rs.getInt("orderID"));
                }
            }
            
            String[] shipmentMethods = {"Standard", "Express", "Priority", "Overnight", "Economy"};
            String[] statuses = {"Pending", "Processing", "Shipped", "Delivered", "Cancelled"};
            
            for (int i = 0; i < 20; i++) {
                int userIndex = i % userIDs.size();
                int orderIndex = i % orderIDs.size();
                int methodIndex = i % shipmentMethods.length;
                int statusIndex = i % statuses.length;
                boolean finalized = statusIndex > 1;
                
                String insertSQL = "INSERT INTO Shipment (orderID, customerID, shipmentMethod, streetAddress, " +
                                  "city, state, postcode, status, trackingNumber, finalized) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setInt(1, orderIDs.get(orderIndex));
                    pstmt.setInt(2, userIDs.get(userIndex));
                    pstmt.setString(3, shipmentMethods[methodIndex]);
                    pstmt.setString(4, "123 Sample St");
                    pstmt.setString(5, "Sydney");
                    pstmt.setString(6, "NSW");
                    pstmt.setString(7, "2000");
                    pstmt.setString(8, statuses[statusIndex]);
                    pstmt.setString(9, finalized ? "TRK" + (10000 + i) : null);
                    pstmt.setBoolean(10, finalized);
                    
                    pstmt.executeUpdate();
                }
            }
            
            LOGGER.log(Level.INFO, "20 sample shipments inserted");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting sample shipments", e);
        }
    }
    
    private Shipment extractShipmentFromResultSet(ResultSet rs) throws SQLException {
        Shipment shipment = new Shipment();
        shipment.setShipmentID(rs.getInt("shipmentID"));
        shipment.setOrderID(rs.getInt("orderID"));
        shipment.setCustomerID(rs.getInt("customerID"));
        shipment.setShipmentMethod(rs.getString("shipmentMethod"));
        shipment.setShipmentDate(rs.getDate("shipmentDate"));
        shipment.setStreetAddress(rs.getString("streetAddress"));
        shipment.setCity(rs.getString("city"));
        shipment.setState(rs.getString("state"));
        shipment.setPostcode(rs.getString("postcode"));
        shipment.setStatus(rs.getString("status"));
        shipment.setTrackingNumber(rs.getString("trackingNumber"));
        shipment.setCreatedDate(rs.getDate("createdDate"));
        shipment.setUpdatedDate(rs.getDate("updatedDate"));
        shipment.setFinalized(rs.getBoolean("finalized"));
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
                      "WHERE shipmentID = ? AND finalized = 0";
        
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
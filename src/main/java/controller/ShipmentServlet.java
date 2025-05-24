package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Shipment;
import model.User;
import model.dao.DBConnector;
import model.dao.ShipmentDAO;

/**
 * Main servlet controller for shipment management
 * Handles CRUD operations Create, Read, Update, Delete, and Finalize
 * User security checks for all operations
 */
@WebServlet(name = "ShipmentServlet", urlPatterns = {"/ShipmentServlet"})
public class ShipmentServlet extends HttpServlet {
    
    /**
     * Handles GET requests (viewing and displaying data)
     * Button actions are based on the 'action' parameter
     * Default action is 'list' if action is null or empty 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Security check - redirect unlogged in users
        if (user == null) {
            session.setAttribute("errorMsg", "Please log in to access shipment management.");
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            // Initialize database connection and DAO
            DBConnector db = new DBConnector();
            Connection conn = db.openConnection();
            ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
            
            // Route to appropriate action handler
            String action = request.getParameter("action");
            if (action == null) {
                action = "list";
            }
            
            switch (action) {
                case "view":
                    viewShipment(request, response, shipmentDAO, user);
                    break;
                case "showCreate":
                    showCreateForm(request, response, user);
                    break;
                case "showEdit":
                    showEditForm(request, response, shipmentDAO, user);
                    break;
                case "search":
                    searchShipments(request, response, shipmentDAO, user);
                    break;
                default:
                    listShipments(request, response, shipmentDAO, user);
                    break;
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Handles POST requests for modifying data
     * Button actions are based on the 'action' parameter
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Security check - redirect unlogged in users
        if (user == null) {
            session.setAttribute("errorMsg", "Please log in to access shipment management.");
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            // Initialize database connection and DAO
            DBConnector db = new DBConnector();
            Connection conn = db.openConnection();
            ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
            
            // Route to appropriate action handler
            String action = request.getParameter("action");
            if (action == null) {
                action = "list";
            }
            
            switch (action) {
                case "create":
                    createShipment(request, response, shipmentDAO, user);
                    break;
                case "update":
                    updateShipment(request, response, shipmentDAO, user);
                    break;
                case "delete":
                    deleteShipment(request, response, shipmentDAO, user);
                    break;
                case "finalize":
                    finalizeShipment(request, response, shipmentDAO, user);
                    break;
                default:
                    listShipments(request, response, shipmentDAO, user);
                    break;
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Displays list of shipments for the current user
     * Shows all shipments ordered by most recent update first
     */
    private void listShipments(HttpServletRequest request, HttpServletResponse response, 
                              ShipmentDAO shipmentDAO, User user) 
                              throws ServletException, IOException, SQLException {
        
        List<Shipment> shipments = shipmentDAO.getShipmentsByCustomerId(user.getId());
        request.setAttribute("shipments", shipments);
        request.getRequestDispatcher("shipment-list.jsp").forward(request, response);
    }
    
    private void viewShipment(HttpServletRequest request, HttpServletResponse response, 
                             ShipmentDAO shipmentDAO, User user) 
                             throws ServletException, IOException, SQLException {
        
        String shipmentIdStr = request.getParameter("id");
        
        if (shipmentIdStr == null || shipmentIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "Shipment ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        try {
            int shipmentId = Integer.parseInt(shipmentIdStr);
            Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
            
            if (shipment == null) {
                request.setAttribute("errorMessage", "Shipment not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            if (shipment.getCustomerID() != user.getId() && !user.isAdmin() && !user.isStaff()) {
                request.setAttribute("errorMessage", "You do not have permission to view this shipment.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("shipment", shipment);
            request.getRequestDispatcher("shipment-view.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid shipment ID format.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Displays the shipment creation form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response, User user) 
                               throws ServletException, IOException {
        
        request.setAttribute("userId", user.getId());
        request.getRequestDispatcher("shipment-create.jsp").forward(request, response);
    }
    
    /**
     * Validates all required fields and creates shipment with default status
     * Redirects to view page after successful creation
     */
    private void createShipment(HttpServletRequest request, HttpServletResponse response, 
                               ShipmentDAO shipmentDAO, User user) 
                               throws ServletException, IOException, SQLException {
        
        // Extract form parameters
        String orderIdStr = request.getParameter("orderId");
        String shipmentMethod = request.getParameter("shipmentMethod");
        String streetAddress = request.getParameter("streetAddress");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String postcode = request.getParameter("postcode");
        
        // Validate required fields
        if (orderIdStr == null || orderIdStr.isEmpty() || 
            shipmentMethod == null || shipmentMethod.isEmpty() ||
            streetAddress == null || streetAddress.isEmpty() ||
            city == null || city.isEmpty() ||
            state == null || state.isEmpty() ||
            postcode == null || postcode.isEmpty()) {
            
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("shipment-create.jsp").forward(request, response);
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            
            // Create new shipment object with form data
            Shipment shipment = new Shipment();
            shipment.setOrderID(orderId);
            shipment.setCustomerID(user.getId());
            shipment.setShipmentMethod(shipmentMethod);
            shipment.setStreetAddress(streetAddress);
            shipment.setCity(city);
            shipment.setState(state);
            shipment.setPostcode(postcode);
            shipment.setStatus("Pending");
            shipment.setFinalized(false);
            
            // Save to database and redirect to view
            int shipmentId = shipmentDAO.createShipment(shipment);
            response.sendRedirect("ShipmentServlet?action=view&id=" + shipmentId);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid order ID format.");
            request.getRequestDispatcher("shipment-create.jsp").forward(request, response);
        }
    }
    
    /**
     * Displays shipment edit form with current data pre-populated
     * Only non-finalized shipments can be edited
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, 
                             ShipmentDAO shipmentDAO, User user) 
                             throws ServletException, IOException, SQLException {
        
        String shipmentIdStr = request.getParameter("id");
        
        if (shipmentIdStr == null || shipmentIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "Shipment ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        try {
            int shipmentId = Integer.parseInt(shipmentIdStr);
            Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
            
            if (shipment == null) {
                request.setAttribute("errorMessage", "Shipment not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            if (shipment.getCustomerID() != user.getId() && !user.isAdmin() && !user.isStaff()) {
                request.setAttribute("errorMessage", "You do not have permission to edit this shipment.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Business rule - finalized shipments cannot be edited
            if (shipment.isFinalized()) {
                request.setAttribute("errorMessage", "This shipment is already finalized and cannot be edited.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("shipment", shipment);
            request.getRequestDispatcher("shipment-edit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid shipment ID format.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Processes shipment update from edit form submission
     * Updates shipment data and redirects to view page
     */
    private void updateShipment(HttpServletRequest request, HttpServletResponse response, 
                               ShipmentDAO shipmentDAO, User user) 
                               throws ServletException, IOException, SQLException {
        
        String shipmentIdStr = request.getParameter("shipmentId");
        String orderIdStr = request.getParameter("orderId");
        String shipmentMethod = request.getParameter("shipmentMethod");
        String streetAddress = request.getParameter("streetAddress");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String postcode = request.getParameter("postcode");
        
        if (shipmentIdStr == null || shipmentIdStr.isEmpty() ||
            orderIdStr == null || orderIdStr.isEmpty() || 
            shipmentMethod == null || shipmentMethod.isEmpty() ||
            streetAddress == null || streetAddress.isEmpty() ||
            city == null || city.isEmpty() ||
            state == null || state.isEmpty() ||
            postcode == null || postcode.isEmpty()) {
            
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("shipment-edit.jsp").forward(request, response);
            return;
        }
        
        try {
            int shipmentId = Integer.parseInt(shipmentIdStr);
            int orderId = Integer.parseInt(orderIdStr);
            
            Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
            
            if (shipment == null) {
                request.setAttribute("errorMessage", "Shipment not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            if (shipment.getCustomerID() != user.getId() && !user.isAdmin() && !user.isStaff()) {
                request.setAttribute("errorMessage", "You do not have permission to edit this shipment.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            if (shipment.isFinalized()) {
                request.setAttribute("errorMessage", "This shipment is already finalized and cannot be edited.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            shipment.setOrderID(orderId);
            shipment.setShipmentMethod(shipmentMethod);
            shipment.setStreetAddress(streetAddress);
            shipment.setCity(city);
            shipment.setState(state);
            shipment.setPostcode(postcode);
            
            // Save changes and redirect
            boolean success = shipmentDAO.updateShipment(shipment);
            
            if (success) {
                response.sendRedirect("ShipmentServlet?action=view&id=" + shipmentId);
            } else {
                request.setAttribute("errorMessage", "Failed to update shipment. Please try again.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid ID format.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Handles shipment deletion requests
     * Only non-finalized shipments can be deleted
     */
    private void deleteShipment(HttpServletRequest request, HttpServletResponse response, 
                               ShipmentDAO shipmentDAO, User user) 
                               throws ServletException, IOException, SQLException {
        
        String shipmentIdStr = request.getParameter("id");
        
        if (shipmentIdStr == null || shipmentIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "Shipment ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        try {
            int shipmentId = Integer.parseInt(shipmentIdStr);
            
            // Retrieve shipment for validation
            Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
            
            if (shipment == null) {
                request.setAttribute("errorMessage", "Shipment not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Security check - user validation
            if (shipment.getCustomerID() != user.getId() && !user.isAdmin() && !user.isStaff()) {
                request.setAttribute("errorMessage", "You do not have permission to delete this shipment.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Business rule - finalized shipments cannot be deleted
            if (shipment.isFinalized()) {
                request.setAttribute("errorMessage", "This shipment is already finalized and cannot be deleted.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Delete and redirect
            boolean success = shipmentDAO.deleteShipment(shipmentId);
            
            if (success) {
                response.sendRedirect("ShipmentServlet?message=Shipment+deleted+successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to delete shipment. Please try again.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid shipment ID format.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Finalizes shipment with tracking number and prevents further changes
     * Auto-generates tracking number if none provided
     */
    private void finalizeShipment(HttpServletRequest request, HttpServletResponse response, 
                                 ShipmentDAO shipmentDAO, User user) 
                                 throws ServletException, IOException, SQLException {
        
        String shipmentIdStr = request.getParameter("id");
        String trackingNumber = request.getParameter("trackingNumber");
        
        if (shipmentIdStr == null || shipmentIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "Shipment ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        try {
            int shipmentId = Integer.parseInt(shipmentIdStr);
            
            // Retrieve shipment for validation
            Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
            
            if (shipment == null) {
                request.setAttribute("errorMessage", "Shipment not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Security check - user validation
            if (shipment.getCustomerID() != user.getId() && !user.isAdmin() && !user.isStaff()) {
                request.setAttribute("errorMessage", "You do not have permission to finalize this shipment.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Business rule - already finalized shipments cannot be re-finalized
            if (shipment.isFinalized()) {
                request.setAttribute("errorMessage", "This shipment is already finalized.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Auto-generate tracking number if not provided
            if (trackingNumber == null || trackingNumber.isEmpty()) {
                trackingNumber = "TRK" + System.currentTimeMillis();
            }
            
            // Finalize shipment and redirect
            boolean success = shipmentDAO.finalizeShipment(shipmentId, trackingNumber);
            
            if (success) {
                response.sendRedirect("ShipmentServlet?action=view&id=" + shipmentId);
            } else {
                request.setAttribute("errorMessage", "Failed to finalize shipment. Please try again.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid shipment ID format.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Handles shipment search functionality
     */
    private void searchShipments(HttpServletRequest request, HttpServletResponse response, 
                                ShipmentDAO shipmentDAO, User user) 
                                throws ServletException, IOException, SQLException {
        
        // Extract search parameters
        String shipmentIdStr = request.getParameter("shipmentId");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        Integer shipmentId = null;
        Date startDate = null;
        Date endDate = null;
        
        // Parse shipment ID if provided
        if (shipmentIdStr != null && !shipmentIdStr.isEmpty()) {
            try {
                shipmentId = Integer.parseInt(shipmentIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid shipment ID format.");
                request.getRequestDispatcher("shipment-search.jsp").forward(request, response);
                return;
            }
        }
        
        // Parse dates with strict format validation
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        if (startDateStr != null && !startDateStr.isEmpty()) {
            try {
                java.util.Date parsedDate = dateFormat.parse(startDateStr);
                startDate = new Date(parsedDate.getTime());
            } catch (ParseException e) {
                request.setAttribute("errorMessage", "Invalid start date format. Please use YYYY-MM-DD.");
                request.getRequestDispatcher("shipment-search.jsp").forward(request, response);
                return;
            }
        }
        
        if (endDateStr != null && !endDateStr.isEmpty()) {
            try {
                java.util.Date parsedDate = dateFormat.parse(endDateStr);
                endDate = new Date(parsedDate.getTime());
            } catch (ParseException e) {
                request.setAttribute("errorMessage", "Invalid end date format. Please use YYYY-MM-DD.");
                request.getRequestDispatcher("shipment-search.jsp").forward(request, response);
                return;
            }
        }
        
        // Search and display results
        List<Shipment> shipments = shipmentDAO.searchShipments(user.getId(), startDate, endDate, shipmentId);
        
        request.setAttribute("shipments", shipments);
        request.setAttribute("shipmentId", shipmentIdStr);
        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);
        request.getRequestDispatcher("shipment-search.jsp").forward(request, response);
    }
}
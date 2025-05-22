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

@WebServlet(name = "ShipmentServlet", urlPatterns = {"/ShipmentServlet"})
public class ShipmentServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            session.setAttribute("errorMsg", "Please log in to access shipment management.");
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            // Initialize database connection
            DBConnector db = new DBConnector();
            Connection conn = db.openConnection();
            ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
            
            // Get the action parameter
            String action = request.getParameter("action");
            if (action == null) {
                action = "list"; // Default action
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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            session.setAttribute("errorMsg", "Please log in to access shipment management.");
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            // Initialize database connection
            DBConnector db = new DBConnector();
            Connection conn = db.openConnection();
            ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
            
            // Get the action parameter
            String action = request.getParameter("action");
            if (action == null) {
                action = "list"; // Default action
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
    
    // Handler methods
    
    private void listShipments(HttpServletRequest request, HttpServletResponse response, 
                              ShipmentDAO shipmentDAO, User user) 
                              throws ServletException, IOException, SQLException {
        
        System.out.println("Loading shipments for user ID: " + user.getId());
        
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
            
            // Check if the shipment belongs to the logged-in user
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
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response, User user) 
                               throws ServletException, IOException {
        
        // Set any default values or attributes needed for the form
        request.setAttribute("userId", user.getId());
        request.getRequestDispatcher("shipment-create.jsp").forward(request, response);
    }
    
    private void createShipment(HttpServletRequest request, HttpServletResponse response, 
                               ShipmentDAO shipmentDAO, User user) 
                               throws ServletException, IOException, SQLException {
        
        // Get form parameters
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
            
            // Create a new shipment object
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
            
            // Save to database
            int shipmentId = shipmentDAO.createShipment(shipment);
            
            // Redirect to the shipment details page
            response.sendRedirect("ShipmentServlet?action=view&id=" + shipmentId);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid order ID format.");
            request.getRequestDispatcher("shipment-create.jsp").forward(request, response);
        }
    }
    
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
            
            // Check if the shipment belongs to the logged-in user
            if (shipment.getCustomerID() != user.getId() && !user.isAdmin() && !user.isStaff()) {
                request.setAttribute("errorMessage", "You do not have permission to edit this shipment.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Check if the shipment is already finalized
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
    
    private void updateShipment(HttpServletRequest request, HttpServletResponse response, 
                               ShipmentDAO shipmentDAO, User user) 
                               throws ServletException, IOException, SQLException {
        
        // Get form parameters
        String shipmentIdStr = request.getParameter("shipmentId");
        String orderIdStr = request.getParameter("orderId");
        String shipmentMethod = request.getParameter("shipmentMethod");
        String streetAddress = request.getParameter("streetAddress");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String postcode = request.getParameter("postcode");
        
        // Validate required fields
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
            
            // Get existing shipment
            Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
            
            if (shipment == null) {
                request.setAttribute("errorMessage", "Shipment not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Check if the shipment belongs to the logged-in user
            if (shipment.getCustomerID() != user.getId() && !user.isAdmin() && !user.isStaff()) {
                request.setAttribute("errorMessage", "You do not have permission to edit this shipment.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Check if the shipment is already finalized
            if (shipment.isFinalized()) {
                request.setAttribute("errorMessage", "This shipment is already finalized and cannot be edited.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Update shipment details
            shipment.setOrderID(orderId);
            shipment.setShipmentMethod(shipmentMethod);
            shipment.setStreetAddress(streetAddress);
            shipment.setCity(city);
            shipment.setState(state);
            shipment.setPostcode(postcode);
            
            // Save to database
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
            
            // Get existing shipment
            Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
            
            if (shipment == null) {
                request.setAttribute("errorMessage", "Shipment not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Check if the shipment belongs to the logged-in user
            if (shipment.getCustomerID() != user.getId() && !user.isAdmin() && !user.isStaff()) {
                request.setAttribute("errorMessage", "You do not have permission to delete this shipment.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Check if the shipment is already finalized
            if (shipment.isFinalized()) {
                request.setAttribute("errorMessage", "This shipment is already finalized and cannot be deleted.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Delete the shipment
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
            
            // Get existing shipment
            Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
            
            if (shipment == null) {
                request.setAttribute("errorMessage", "Shipment not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Check if the shipment belongs to the logged-in user
            if (shipment.getCustomerID() != user.getId() && !user.isAdmin() && !user.isStaff()) {
                request.setAttribute("errorMessage", "You do not have permission to finalize this shipment.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Check if the shipment is already finalized
            if (shipment.isFinalized()) {
                request.setAttribute("errorMessage", "This shipment is already finalized.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Generate a tracking number if none provided
            if (trackingNumber == null || trackingNumber.isEmpty()) {
                trackingNumber = "TRK" + System.currentTimeMillis();
            }
            
            // Finalize the shipment
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
    
    private void searchShipments(HttpServletRequest request, HttpServletResponse response, 
                                ShipmentDAO shipmentDAO, User user) 
                                throws ServletException, IOException, SQLException {
        
        String shipmentIdStr = request.getParameter("shipmentId");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        // Convert parameters to appropriate types
        Integer shipmentId = null;
        Date startDate = null;
        Date endDate = null;
        
        if (shipmentIdStr != null && !shipmentIdStr.isEmpty()) {
            try {
                shipmentId = Integer.parseInt(shipmentIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid shipment ID format.");
                request.getRequestDispatcher("shipment-search.jsp").forward(request, response);
                return;
            }
        }
        
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
        
        // Perform search
        List<Shipment> shipments = shipmentDAO.searchShipments(user.getId(), startDate, endDate, shipmentId);
        
        // Set attributes and forward to search results page
        request.setAttribute("shipments", shipments);
        request.setAttribute("shipmentId", shipmentIdStr);
        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);
        request.getRequestDispatcher("shipment-search.jsp").forward(request, response);
    }
}
package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

@WebServlet(name = "ShipmentSearchServlet", urlPatterns = {"/ShipmentSearchServlet"})
public class ShipmentSearchServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            session.setAttribute("errorMsg", "Please log in to search shipments.");
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Debug: Print user ID
        System.out.println("Search requested by user ID: " + user.getId());
        
        // Just show the search form if no search parameters
        if (request.getParameter("shipmentId") == null && 
            request.getParameter("startDate") == null && 
            request.getParameter("endDate") == null) {
            
            request.getRequestDispatcher("shipment-search.jsp").forward(request, response);
            return;
        }
        
        // Debug: Parameters received
        
        // Process the search
        try {
            // Initialize database connection
            DBConnector db = new DBConnector();
            Connection conn = db.openConnection();
            ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
            
            // Get search parameters
            String shipmentIdStr = request.getParameter("shipmentId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            
            // Debug
            
            // Initialize search variables
            Integer shipmentId = null;
            java.sql.Date startDate = null;
            java.sql.Date endDate = null;
            
            // Parse shipment ID
            if (shipmentIdStr != null && !shipmentIdStr.trim().isEmpty()) {
                try {
                    shipmentId = Integer.parseInt(shipmentIdStr.trim());
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid shipment ID format. Please enter a number.");
                }
            }
            
            // Parse dates with multiple formats
            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                startDate = parseDateWithMultipleFormats(startDateStr);
                if (startDate == null) {
                    request.setAttribute("errorMessage", "Invalid start date format. Please use YYYY-MM-DD or DD/MM/YYYY.");
                } else {
                }
            }
            
            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                endDate = parseDateWithMultipleFormats(endDateStr);
                if (endDate == null) {
                    request.setAttribute("errorMessage", "Invalid end date format. Please use YYYY-MM-DD or DD/MM/YYYY.");
                } else {
                }
            }
            
            // Perform search
            List<Shipment> shipments;
            
            if (shipmentId != null) {
                // If searching by ID, first try direct lookup
                Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
                shipments = new ArrayList<>();
                
                if (shipment != null && shipment.getCustomerID() == user.getId()) {
                    shipments.add(shipment);
                }
            } else {
                // Otherwise use full search
                shipments = shipmentDAO.searchShipments(user.getId(), startDate, endDate, null);
                System.out.println("Found " + shipments.size() + " shipments with specified criteria");
            }
            
            // Set attributes and forward to search results page
            request.setAttribute("shipments", shipments);
            request.setAttribute("shipmentId", shipmentIdStr);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            request.getRequestDispatcher("shipment-search.jsp").forward(request, response);
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Attempts to parse a date string using multiple formats
     */
    private java.sql.Date parseDateWithMultipleFormats(String dateStr) {
        // Try different date formats
        String[] dateFormats = {
            "yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy", "dd-MM-yyyy", "yyyy/MM/dd"
        };
        
        for (String format : dateFormats) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                dateFormat.setLenient(false);
                java.util.Date parsedDate = dateFormat.parse(dateStr.trim());
                return new java.sql.Date(parsedDate.getTime());
            } catch (ParseException e) {
                // Try next format
            }
        }
        
        return null; // Could not parse with any format
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
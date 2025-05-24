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

/**
 * Servlet for searching for a shipment object by either ID or creation date 
 * Supports multiple date formats
 * Instructs both how the form displays and the search logic
 */
@WebServlet(name = "ShipmentSearchServlet", urlPatterns = {"/ShipmentSearchServlet"})
public class ShipmentSearchServlet extends HttpServlet {
    
    /**
     * Handles displaying search form and search functionality 
     * GET with no parameters shows default search form
     * GET with parameters executes search and displays results
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Security check - redirect not logged in users
        if (user == null) {
            session.setAttribute("errorMsg", "Please log in to search shipments.");
            response.sendRedirect("login.jsp");
            return;
        }
        
        System.out.println("Search requested by user ID: " + user.getId());
        
        // Show search form if all fields null
        if (request.getParameter("shipmentId") == null && 
            request.getParameter("startDate") == null && 
            request.getParameter("endDate") == null) {
            
            request.getRequestDispatcher("shipment-search.jsp").forward(request, response);
            return;
        }
        
        // Execute search if there are any fields
        try {
            DBConnector db = new DBConnector();
            Connection conn = db.openConnection();
            ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
            
            // Get search parameters and check format
            String shipmentIdStr = request.getParameter("shipmentId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            Integer shipmentId = null;
            java.sql.Date startDate = null;
            java.sql.Date endDate = null;
            
            // Parse shipment ID with null and empty check to ensure parameters are extracted successfully
            if (shipmentIdStr != null && !shipmentIdStr.trim().isEmpty()) {
                try {
                    shipmentId = Integer.parseInt(shipmentIdStr.trim());
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid shipment ID format. Please enter a number.");
                }
            }
            
            // Parse start date with multiple format support
            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                startDate = parseDateWithMultipleFormats(startDateStr);
                if (startDate == null) {
                    request.setAttribute("errorMessage", "Invalid start date format. Please use YYYY-MM-DD or DD/MM/YYYY.");
                } else {
                    System.out.println("Parsed start date: " + startDate);
                }
            }
            
            // Parse end date with multiple format support
            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                endDate = parseDateWithMultipleFormats(endDateStr);
                if (endDate == null) {
                    request.setAttribute("errorMessage", "Invalid end date format. Please use YYYY-MM-DD or DD/MM/YYYY.");
                } else {
                    System.out.println("Parsed end date: " + endDate);
                }
            }
            
            List<Shipment> shipments;
            
            // Search shipment by Id
            if (shipmentId != null) {
                // Get shipment object by Id
                Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
                shipments = new ArrayList<>();
                
                // Security check - correct logged in user 
                if (shipment != null && shipment.getCustomerID() == user.getId()) {
                    shipments.add(shipment);
                }
            } else {
                // Search database for multiple shipments using User Id and/or creation date
                shipments = shipmentDAO.searchShipments(user.getId(), startDate, endDate, null);
                System.out.println("Found " + shipments.size() + " shipments with specified criteria");
            }
            
            // Set shipments and attributes being displayed in the JSP shipment search form 
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
     * Multi format date parser
     * Tries all standard date formats
     * 
     * @param dateStr Date string in various formats
     * @return SQL Date object, null if parsing fails
     */
    private java.sql.Date parseDateWithMultipleFormats(String dateStr) {
        // Date formats
        String[] dateFormats = {
            "yyyy-MM-dd",   
            "dd/MM/yyyy", 
            "MM/dd/yyyy",  
            "dd-MM-yyyy",   
            "yyyy/MM/dd"   
        };
        
        for (String format : dateFormats) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                dateFormat.setLenient(false); // Strict parsing into SimpleDateFormat 
                java.util.Date parsedDate = dateFormat.parse(dateStr.trim());
                return new java.sql.Date(parsedDate.getTime());
            } catch (ParseException e) {
                // Try next format and log failure
                System.out.println("Failed to parse '" + dateStr + "' with format: " + format);
            }
        }
        
        System.out.println("All date format attempts failed for: " + dateStr);
        return null;
    }
    
    /**
     * Handles POST requests by fowarding to GET
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
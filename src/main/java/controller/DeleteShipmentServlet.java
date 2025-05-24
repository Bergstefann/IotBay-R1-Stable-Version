package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
 * Dedicated servlet for the deleting shipment objects process
 */
@WebServlet(name = "DeleteShipmentServlet", urlPatterns = {"/DeleteShipmentServlet"})
public class DeleteShipmentServlet extends HttpServlet {

    /**
     * Handles GET requests for deleting shipments
     * Validation and deletion are in a single request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Security check - redirect users who are not logged in to login page
        if (user == null) {
            session.setAttribute("errorMsg", "Please log in to delete shipments.");
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Parse and validate shipment ID
        String shipmentIdStr = request.getParameter("id");
        System.out.println("Delete request received for shipment ID: " + shipmentIdStr);
        
        if (shipmentIdStr == null || shipmentIdStr.isEmpty()) {
            response.sendRedirect("ShipmentServlet");
            return;
        }
        
        try {
            int shipmentId = Integer.parseInt(shipmentIdStr);
            
            // Initialize DB connection and shipmentDAO
            DBConnector db = new DBConnector();
            Connection conn = db.openConnection();
            ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
            
            // Retrieve shipment using id and null check
            Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
            
            if (shipment == null) {
                response.sendRedirect("ShipmentServlet?error=Shipment not found");
                return;
            }
            
            // Security check - shipment customer is logged in user
            if (shipment.getCustomerID() != user.getId()) {
                response.sendRedirect("ShipmentServlet?error=Not authorized to delete this shipment");
                return;
            }
            
            // Business rule - finalized shipments cannot be deleted
            if (shipment.isFinalized()) {
                response.sendRedirect("ShipmentServlet?error=Cannot delete finalized shipment");
                return;
            }
            
            // Delete shipment and error handling
            boolean success = shipmentDAO.deleteShipment(shipmentId);
            
            if (success) {
                System.out.println("Successfully deleted shipment: " + shipmentId);
                response.sendRedirect("ShipmentServlet?message=Shipment deleted successfully");
            } else {
                response.sendRedirect("ShipmentServlet?error=Failed to delete shipment");
            }
            
        } catch (NumberFormatException e) {
            // Handle invalid ID format
            response.sendRedirect("ShipmentServlet?error=Invalid shipment ID");
        } catch (ClassNotFoundException | SQLException e) {
            // Handle database errors
            e.printStackTrace();
            response.sendRedirect("ShipmentServlet?error=Database error");
        }
    }

    /**
     * Handles POST requests by forwarding GET handler
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
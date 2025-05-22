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

@WebServlet(name = "DeleteShipmentServlet", urlPatterns = {"/DeleteShipmentServlet"})
public class DeleteShipmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            session.setAttribute("errorMsg", "Please log in to delete shipments.");
            response.sendRedirect("login.jsp");
            return;
        }
        
        String shipmentIdStr = request.getParameter("id");
        System.out.println("Delete request received for shipment ID: " + shipmentIdStr);
        
        if (shipmentIdStr == null || shipmentIdStr.isEmpty()) {
            response.sendRedirect("ShipmentServlet");
            return;
        }
        
        try {
            int shipmentId = Integer.parseInt(shipmentIdStr);
            
            // Initialize database connection
            DBConnector db = new DBConnector();
            Connection conn = db.openConnection();
            ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
            
            // Get the shipment first
            Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
            
            if (shipment == null) {
                response.sendRedirect("ShipmentServlet?error=Shipment not found");
                return;
            }
            
            // Check if it belongs to the current user
            if (shipment.getCustomerID() != user.getId()) {
                response.sendRedirect("ShipmentServlet?error=Not authorized to delete this shipment");
                return;
            }
            
            // Check if it's finalized
            if (shipment.isFinalized()) {
                response.sendRedirect("ShipmentServlet?error=Cannot delete finalized shipment");
                return;
            }
            
            // Try to delete
            boolean success = shipmentDAO.deleteShipment(shipmentId);
            
            if (success) {
                System.out.println("Successfully deleted shipment: " + shipmentId);
                response.sendRedirect("ShipmentServlet?message=Shipment deleted successfully");
            } else {
                response.sendRedirect("ShipmentServlet?error=Failed to delete shipment");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("ShipmentServlet?error=Invalid shipment ID");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("ShipmentServlet?error=Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
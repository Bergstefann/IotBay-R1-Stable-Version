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
        
        if (user == null) {
            session.setAttribute("errorMsg", "Please log in to search shipments.");
            response.sendRedirect("login.jsp");
            return;
        }
        
        System.out.println("Search requested by user ID: " + user.getId());
        
        if (request.getParameter("shipmentId") == null && 
            request.getParameter("startDate") == null && 
            request.getParameter("endDate") == null) {
            
            request.getRequestDispatcher("shipment-search.jsp").forward(request, response);
            return;
        }
        
        try {
            DBConnector db = new DBConnector();
            Connection conn = db.openConnection();
            ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
            
            String shipmentIdStr = request.getParameter("shipmentId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            Integer shipmentId = null;
            java.sql.Date startDate = null;
            java.sql.Date endDate = null;
            
            if (shipmentIdStr != null && !shipmentIdStr.trim().isEmpty()) {
                try {
                    shipmentId = Integer.parseInt(shipmentIdStr.trim());
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid shipment ID format. Please enter a number.");
                }
            }
            
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
            
            List<Shipment> shipments;
            
            if (shipmentId != null) {
                Shipment shipment = shipmentDAO.getShipmentById(shipmentId);
                shipments = new ArrayList<>();
                
                if (shipment != null && shipment.getCustomerID() == user.getId()) {
                    shipments.add(shipment);
                }
            } else {
                shipments = shipmentDAO.searchShipments(user.getId(), startDate, endDate, null);
                System.out.println("Found " + shipments.size() + " shipments with specified criteria");
            }
            
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

    private java.sql.Date parseDateWithMultipleFormats(String dateStr) {
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
            }
        }
        
        return null;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
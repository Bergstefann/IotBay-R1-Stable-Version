<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@page import="model.Shipment"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Search Shipments - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .search-container {
                background-color: white;
                padding: 2rem;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                margin-bottom: 2rem;
            }
            
            .search-header {
                margin-bottom: 1.5rem;
                padding-bottom: 1rem;
                border-bottom: 1px solid #eee;
            }
            
            .search-form {
                display: flex;
                flex-wrap: wrap;
                gap: 15px;
                margin-bottom: 1.5rem;
            }
            
            .form-group {
                flex: 1;
                min-width: 250px;
            }
            
            .form-actions {
                width: 100%;
                display: flex;
                justify-content: flex-end;
                margin-top: 1rem;
            }
            
            .results-container {
                background-color: white;
                padding: 2rem;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }
            
            .results-header {
                margin-bottom: 1.5rem;
                padding-bottom: 1rem;
                border-bottom: 1px solid #eee;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            
            .shipment-table {
                width: 100%;
                border-collapse: collapse;
            }
            
            .shipment-table th,
            .shipment-table td {
                padding: 0.8rem;
                text-align: left;
                border-bottom: 1px solid #eee;
            }
            
            .shipment-table th {
                background-color: #f5f5f5;
                font-weight: bold;
            }
            
            .status-badge {
                display: inline-block;
                padding: 0.25rem 0.5rem;
                border-radius: 4px;
                font-size: 0.8rem;
                font-weight: bold;
            }
            
            .status-pending {
                background-color: #ffc107;
                color: #212529;
            }
            
            .status-processing {
                background-color: #17a2b8;
                color: white;
            }
            
            .status-shipped {
                background-color: #28a745;
                color: white;
            }
            
            .status-delivered {
                background-color: #6c757d;
                color: white;
            }
            
            .status-cancelled {
                background-color: #dc3545;
                color: white;
            }
            
            .empty-results {
                text-align: center;
                padding: 3rem;
                background-color: #f9f9f9;
                border-radius: 8px;
            }
        </style>
    </head>

    <body>
        <%@ include file="header.jsp" %>
        
        <%
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            String shipmentIdStr = (String) request.getAttribute("shipmentId");
            String startDateStr = (String) request.getAttribute("startDate");
            String endDateStr = (String) request.getAttribute("endDate");
            List<Shipment> shipments = (List<Shipment>) request.getAttribute("shipments");
            
            String errorMessage = (String) request.getAttribute("errorMessage");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        %>

        <main>
            <div class="search-container">
                <h2>Search Shipments</h2>
                
                <form action="ShipmentSearchServlet" method="get">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="shipmentId">Shipment ID</label>
                            <input type="number" id="shipmentId" name="shipmentId" 
                                value="<%= request.getAttribute("shipmentId") != null ? request.getAttribute("shipmentId") : "" %>">
                        </div>
                        
                        <div class="form-group">
                            <label for="startDate">Creation Date</label>
                            <input type="date" id="startDate" name="startDate" 
                                value="<%= request.getAttribute("startDate") != null ? request.getAttribute("startDate") : "" %>">
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn">Search</button>
                    </div>
                </form>
            </div>
            
            <% if (shipments != null) { %>
                <div class="results-container">
                    <div class="results-header">
                        <h2>Search Results</h2>
                        <span><%= shipments.size() %> shipment(s) found</span>
                    </div>
                    
                    <% if (shipments.isEmpty()) { %>
                        <div class="empty-results">
                            <h3>No shipments found</h3>
                            <p>Try adjusting your search criteria.</p>
                        </div>
                    <% } else { %>
                        <table class="shipment-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Order ID</th>
                                    <th>Method</th>
                                    <th>Address</th>
                                    <th>Status</th>
                                    <th>Created</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Shipment shipment : shipments) { %>
                                    <tr>
                                        <td><%= shipment.getShipmentID() %></td>
                                        <td><%= shipment.getOrderID() %></td>
                                        <td><%= shipment.getShipmentMethod() %></td>
                                        <td><%= shipment.getStreetAddress() + ", " + shipment.getCity() %></td>
                                        <td>
                                            <span class="status-badge status-<%= shipment.getStatus().toLowerCase() %>">
                                                <%= shipment.getStatus() %>
                                            </span>
                                        </td>
                                        <td>
                                            <% if (shipment.getCreatedDate() != null) { %>
                                                <%= dateFormat.format(shipment.getCreatedDate()) %>
                                            <% } else { %>
                                                Not available
                                            <% } %>
                                        </td>
                                        <td>
                                            <a href="ShipmentServlet?action=view&id=<%= shipment.getShipmentID() %>" class="btn btn-sm">View</a>
                                        </td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    <% } %>
                </div>
            <% } %>
        </main>

        <%@ include file="footer.jsp" %>
    </body>
</html>
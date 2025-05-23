<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@page import="model.Shipment"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Shipment Details - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .shipment-container {
                background-color: white;
                padding: 2rem;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                margin-bottom: 2rem;
            }
            
            .shipment-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 1.5rem;
                padding-bottom: 1rem;
                border-bottom: 1px solid #eee;
            }
            
            .action-buttons {
                display: flex;
                gap: 10px;
            }
            
            .shipment-status {
                margin-bottom: 1.5rem;
                padding: 1rem;
                border-radius: 8px;
                background-color: #f5f5f5;
            }
            
            .status-label {
                display: inline-block;
                margin-right: 1rem;
                font-weight: bold;
            }
            
            .status-badge {
                display: inline-block;
                padding: 0.5rem 1rem;
                border-radius: 4px;
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
            
            .shipment-details {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
                gap: 2rem;
                margin-bottom: 2rem;
            }
            
            .detail-group h3 {
                margin-bottom: 0.5rem;
                font-size: 1.2rem;
                color: #555;
            }
            
            .detail-item {
                margin-bottom: 0.5rem;
            }
            
            .detail-label {
                font-weight: bold;
                display: block;
                margin-bottom: 0.25rem;
                color: #777;
            }
            
            .detail-value {
                color: #333;
            }
            
            .tracking-section {
                margin-top: 2rem;
                padding-top: 1.5rem;
                border-top: 1px solid #eee;
            }
            
            .tracking-number {
                font-size: 1.2rem;
                font-weight: bold;
                padding: 0.5rem 1rem;
                background-color: #f8f9fa;
                border: 1px dashed #ccc;
                border-radius: 4px;
                display: inline-block;
                margin-top: 0.5rem;
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
            
            Shipment shipment = (Shipment) request.getAttribute("shipment");
            
            if (shipment == null) {
                response.sendRedirect("ShipmentServlet");
                return;
            }
        
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        %>

        <main>
            <div class="shipment-container">
                <div class="shipment-header">
                    <h1>Shipment #<%= shipment.getShipmentID() %></h1>
                    <div class="action-buttons">
                        <a href="ShipmentServlet" class="btn">Back to List</a>
                        <% if (!shipment.isFinalized()) { %>
                            <a href="ShipmentServlet?action=showEdit&id=<%= shipment.getShipmentID() %>" class="btn">Edit</a>
                            <a href="#" onclick="confirmDelete('<%=shipment.getShipmentID()%>')" class="btn">Delete</a>
                        <% } %>
                    </div>
                </div>
                
                <div class="shipment-status">
                    <span class="status-label">Status:</span>
                    <span class="status-badge status-<%= shipment.getStatus().toLowerCase() %>">
                        <%= shipment.getStatus() %>
                    </span>
                    
                    <% if (shipment.isFinalized()) { %>
                        <span class="finalized-label" style="margin-left: 1rem;">Shipment Finalized</span>
                    <% } %>
                </div>
                
                <div class="shipment-details">
                    <div class="detail-group">
                        <h3>Shipment Information</h3>
                        <div class="detail-item">
                            <span class="detail-label">Order ID</span>
                            <span class="detail-value"><%= shipment.getOrderID() %></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Shipment Method</span>
                            <span class="detail-value"><%= shipment.getShipmentMethod() %></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Created Date</span>
                            <span class="detail-value">
                                <% if (shipment.getCreatedDate() != null) { %>
                                    <%= dateFormat.format(shipment.getCreatedDate()) %>
                                <% } else { %>
                                    Not available
                                <% } %>
                            </span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Last Updated</span>
                            <span class="detail-value">
                                <% if (shipment.getUpdatedDate() != null) { %>
                                    <%= dateFormat.format(shipment.getUpdatedDate()) %>
                                <% } else { %>
                                    Not available
                                <% } %>
                            </span>
                        </div>
                    </div>
                    
                    <div class="detail-group">
                        <h3>Shipping Address</h3>
                        <div class="detail-item">
                            <span class="detail-label">Street Address</span>
                            <span class="detail-value"><%= shipment.getStreetAddress() %></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">City</span>
                            <span class="detail-value"><%= shipment.getCity() %></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">State</span>
                            <span class="detail-value"><%= shipment.getState() %></span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Postcode</span>
                            <span class="detail-value"><%= shipment.getPostcode() %></span>
                        </div>
                    </div>
                </div>
                
                <% if (shipment.isFinalized() && shipment.getTrackingNumber() != null) { %>
                <div class="tracking-section">
                    <h3>Tracking Information</h3>
                    <p>Use the tracking number below to track your shipment:</p>
                    <div class="tracking-number"><%= shipment.getTrackingNumber() %></div>
                </div>
                <% } %>
            </div>
        </main>

        <%@ include file="footer.jsp" %>
        
        <script>
            function confirmDelete(shipmentId) {
                if (confirm("Are you sure you want to delete this shipment? This action cannot be undone.")) {
                    window.location.href = "ShipmentServlet?action=delete&id=" + shipmentId;
                }
            }
        </script>
    </body>
</html>
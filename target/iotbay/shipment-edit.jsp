<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@page import="model.Shipment"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Edit Shipment - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .form-container {
                background-color: white;
                padding: 2rem;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                margin-bottom: 2rem;
            }
            
            .form-header {
                margin-bottom: 1.5rem;
                padding-bottom: 1rem;
                border-bottom: 1px solid #eee;
            }
            
            .form-row {
                display: flex;
                flex-wrap: wrap;
                margin: 0 -10px;
            }
            
            .form-group {
                flex: 1;
                min-width: 250px;
                padding: 0 10px;
                margin-bottom: 1.5rem;
            }
            
            .form-group label {
                display: block;
                margin-bottom: 0.5rem;
                font-weight: 500;
            }
            
            .form-group input, .form-group select {
                width: 100%;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 16px;
            }
            
            .form-group select {
                height: 42px;
            }
            
            .form-actions {
                display: flex;
                justify-content: space-between;
                margin-top: 1rem;
                padding-top: 1rem;
                border-top: 1px solid #eee;
            }
            
            .finalize-section {
                margin-top: 2rem;
                padding-top: 1rem;
                border-top: 1px solid #eee;
            }
            
            .finalize-section h3 {
                margin-bottom: 1rem;
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
            
            String errorMessage = (String) request.getAttribute("errorMessage");
        %>

        <main>
            <div class="form-container">
                <div class="form-header">
                    <h1>Edit Shipment #<%= shipment.getShipmentID() %></h1>
                </div>
                
                <% if (errorMessage != null) { %>
                    <div class="error-message">
                        <%= errorMessage %>
                    </div>
                <% } %>
                
                <form action="ShipmentServlet" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="shipmentId" value="<%= shipment.getShipmentID() %>">
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="orderId">Order ID*</label>
                            <input type="number" id="orderId" name="orderId" value="<%= shipment.getOrderID() %>" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="shipmentMethod">Shipment Method*</label>
                            <select id="shipmentMethod" name="shipmentMethod" required>
                                <option value="">-- Select Method --</option>
                                <option value="Standard" <%= "Standard".equals(shipment.getShipmentMethod()) ? "selected" : "" %>>Standard</option>
                                <option value="Express" <%= "Express".equals(shipment.getShipmentMethod()) ? "selected" : "" %>>Express</option>
                                <option value="Priority" <%= "Priority".equals(shipment.getShipmentMethod()) ? "selected" : "" %>>Priority</option>
                                <option value="Overnight" <%= "Overnight".equals(shipment.getShipmentMethod()) ? "selected" : "" %>>Overnight</option>
                                <option value="Economy" <%= "Economy".equals(shipment.getShipmentMethod()) ? "selected" : "" %>>Economy</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="streetAddress">Street Address*</label>
                            <input type="text" id="streetAddress" name="streetAddress" value="<%= shipment.getStreetAddress() %>" required>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="city">City*</label>
                            <input type="text" id="city" name="city" value="<%= shipment.getCity() %>" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="state">State*</label>
                            <select id="state" name="state" required>
                                <option value="">-- Select State --</option>
                                <option value="NSW" <%= "NSW".equals(shipment.getState()) ? "selected" : "" %>>New South Wales</option>
                                <option value="VIC" <%= "VIC".equals(shipment.getState()) ? "selected" : "" %>>Victoria</option>
                                <option value="QLD" <%= "QLD".equals(shipment.getState()) ? "selected" : "" %>>Queensland</option>
                                <option value="WA" <%= "WA".equals(shipment.getState()) ? "selected" : "" %>>Western Australia</option>
                                <option value="SA" <%= "SA".equals(shipment.getState()) ? "selected" : "" %>>South Australia</option>
                                <option value="TAS" <%= "TAS".equals(shipment.getState()) ? "selected" : "" %>>Tasmania</option>
                                <option value="ACT" <%= "ACT".equals(shipment.getState()) ? "selected" : "" %>>Australian Capital Territory</option>
                                <option value="NT" <%= "NT".equals(shipment.getState()) ? "selected" : "" %>>Northern Territory</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="postcode">Postcode*</label>
                            <input type="text" id="postcode" name="postcode" value="<%= shipment.getPostcode() %>" pattern="[0-9]{4}" title="4-digit postcode" required>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <a href="ShipmentServlet" class="btn">Cancel</a>
                        <button type="submit" class="btn">Update Shipment</button>
                    </div>
                </form>
                
                <div class="finalize-section">
                    <h3>Finalize Shipment</h3>
                    <p>Once finalized, the shipment details cannot be changed.</p>
                    <form action="ShipmentServlet" method="post" id="finalizeForm">
                        <input type="hidden" name="action" value="finalize">
                        <input type="hidden" name="id" value="<%= shipment.getShipmentID() %>">
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="trackingNumber">Tracking Number (optional)</label>
                                <input type="text" id="trackingNumber" name="trackingNumber" placeholder="If left empty, a tracking number will be generated">
                            </div>
                        </div>
                        
                        <button type="button" onclick="confirmFinalize()" class="btn">Finalize Shipment</button>
                    </form>
                </div>
            </div>
        </main>

        <%@ include file="footer.jsp" %>
        
        <script>
            document.querySelector('form').addEventListener('submit', function(e) {
                const postcode = document.getElementById('postcode').value;
                if (!/^\d{4}$/.test(postcode)) {
                    alert('Please enter a valid 4-digit postcode');
                    e.preventDefault();
                }
            });
            
            function confirmFinalize() {
                if (confirm("Are you sure you want to finalize this shipment? This action cannot be undone.")) {
                    document.getElementById('finalizeForm').submit();
                }
            }
        </script>
    </body>
</html>

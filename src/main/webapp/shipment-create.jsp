<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Create Shipment - IoTBay</title>
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
        </style>
    </head>

    <body>
        <%@ include file="header.jsp" %>
        
        <%
            // Check if user is logged in, redirect to login if not
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            // Get any error message
            String errorMessage = (String) request.getAttribute("errorMessage");
        %>

        <main>
            <div class="form-container">
                <div class="form-header">
                    <h1>Create New Shipment</h1>
                </div>
                
                <% if (errorMessage != null) { %>
                    <div class="error-message">
                        <%= errorMessage %>
                    </div>
                <% } %>
                
                <form action="ShipmentServlet" method="post">
                    <input type="hidden" name="action" value="create">
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="orderId">Order ID*</label>
                            <input type="number" id="orderId" name="orderId" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="shipmentMethod">Shipment Method*</label>
                            <select id="shipmentMethod" name="shipmentMethod" required>
                                <option value="">-- Select Method --</option>
                                <option value="Standard">Standard</option>
                                <option value="Express">Express</option>
                                <option value="Priority">Priority</option>
                                <option value="Overnight">Overnight</option>
                                <option value="Economy">Economy</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="streetAddress">Street Address*</label>
                            <input type="text" id="streetAddress" name="streetAddress" required>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="city">City*</label>
                            <input type="text" id="city" name="city" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="state">State*</label>
                            <select id="state" name="state" required>
                                <option value="">-- Select State --</option>
                                <option value="NSW">New South Wales</option>
                                <option value="VIC">Victoria</option>
                                <option value="QLD">Queensland</option>
                                <option value="WA">Western Australia</option>
                                <option value="SA">South Australia</option>
                                <option value="TAS">Tasmania</option>
                                <option value="ACT">Australian Capital Territory</option>
                                <option value="NT">Northern Territory</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="postcode">Postcode*</label>
                            <input type="text" id="postcode" name="postcode" pattern="[0-9]{4}" title="4-digit postcode" required>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <a href="ShipmentServlet" class="btn">Cancel</a>
                        <button type="submit" class="btn">Create Shipment</button>
                    </div>
                </form>
            </div>
        </main>

        <%@ include file="footer.jsp" %>
        
        <script>
            // Client-side validation for reference only - server-side validation is primary
            document.querySelector('form').addEventListener('submit', function(e) {
                const postcode = document.getElementById('postcode').value;
                if (!/^\d{4}$/.test(postcode)) {
                    alert('Please enter a valid 4-digit postcode');
                    e.preventDefault();
                }
            });
        </script>
    </body>
</html>
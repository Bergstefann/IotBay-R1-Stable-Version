<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>My Account - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .account-container {
                background-color: white;
                border-radius: 8px;
                padding: 2rem;
                margin-bottom: 2rem;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }
            
            .account-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 2rem;
                padding-bottom: 1rem;
                border-bottom: 1px solid #eee;
            }
            
            .account-title {
                margin: 0;
                font-size: 1.8rem;
            }
            
            .edit-button {
                padding: 8px 16px;
                background-color: #6c757d;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 0.9rem;
            }
            
            .edit-button:hover {
                background-color: #5a6268;
            }
            
            .user-info {
                margin-bottom: 2rem;
            }
            
            .info-row {
                display: flex;
                margin-bottom: 1rem;
            }
            
            .info-label {
                flex: 0 0 200px;
                font-weight: bold;
                color: #555;
            }
            
            .info-value {
                flex: 1;
                color: #333;
            }
            
            .section-title {
                font-size: 1.4rem;
                margin: 2rem 0 1rem;
                padding-bottom: 0.5rem;
                border-bottom: 1px solid #eee;
            }
            
            .action-buttons {
                margin-top: 2rem;
            }
            
            .btn-danger {
                background-color: #dc3545;
            }
            
            .btn-danger:hover {
                background-color: #c82333;
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
        %>

        <main>
            <div class="account-container">
                <div class="account-header">
                    <h1 class="account-title">My Account</h1>
                    <button class="edit-button" onclick="location.href='edit-account.jsp'">Edit Profile</button>
                </div>
                
                <div class="user-info">
                    <div class="info-row">
                        <div class="info-label">Name:</div>
                        <div class="info-value"><%= user.getFirstName() + " " + user.getLastName() %></div>
                    </div>
                    
                    <div class="info-row">
                        <div class="info-label">Email:</div>
                        <div class="info-value"><%= user.getEmail() %></div>
                    </div>
                    
                    <div class="info-row">
                        <div class="info-label">Phone Number:</div>
                        <div class="info-value"><%= user.getPhone() != null && !user.getPhone().isEmpty() ? user.getPhone() : "Not provided" %></div>
                    </div>
                    
                    <div class="info-row">
                        <div class="info-label">Account Type:</div>
                        <div class="info-value"><%= user.getRole() != null ? user.getRole() : "Customer" %></div>
                    </div>
                </div>
                
                <h2 class="section-title">Address Information</h2>
                
                <div class="user-info">
                    <% if (user.getStreetAddress() != null && !user.getStreetAddress().isEmpty()) { %>
                        <div class="info-row">
                            <div class="info-label">Street Address:</div>
                            <div class="info-value"><%= user.getStreetAddress() %></div>
                        </div>
                        
                        <div class="info-row">
                            <div class="info-label">Suburb:</div>
                            <div class="info-value"><%= user.getSuburb() != null ? user.getSuburb() : "Not provided" %></div>
                        </div>
                        
                        <div class="info-row">
                            <div class="info-label">State:</div>
                            <div class="info-value"><%= user.getState() != null ? user.getState() : "Not provided" %></div>
                        </div>
                        
                        <div class="info-row">
                            <div class="info-label">Postcode:</div>
                            <div class="info-value"><%= user.getPostcode() != null ? user.getPostcode() : "Not provided" %></div>
                        </div>
                        
                        <div class="info-row">
                            <div class="info-label">Country:</div>
                            <div class="info-value"><%= user.getCountry() != null ? user.getCountry() : "Not provided" %></div>
                        </div>
                    <% } else { %>
                        <p>No address information provided. <a href="edit-account.jsp">Add address details</a></p>
                    <% } %>
                </div>
                
                <div class="action-buttons">
                    <a href="change-password.jsp" class="btn">Change Password</a>
                    <a href="LogoutServlet" class="btn">Logout</a>
                    <a href="#" class="btn btn-danger" onclick="confirmDeleteAccount()">Delete Account</a>
                </div>
            </div>
            
            <h2 class="section-title">Order History</h2>
            
            <div class="account-container">
                <p>You haven't placed any orders yet.</p>
                <a href="ProductServlet?action=list" class="btn">Start Shopping</a>
            </div>
        </main>

        <%@ include file="footer.jsp" %>
        
        <script>
            function confirmDeleteAccount() {
                if (confirm("Are you sure you want to delete your account? This action cannot be undone.")) {
                    // In a real application, this would submit a form to delete the account
                    alert("Account deletion is not implemented in this demo.");
                }
            }
        </script>
    </body>
</html>
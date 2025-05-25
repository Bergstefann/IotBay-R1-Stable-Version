<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>

<% 
    User headerUser = (User) session.getAttribute("user"); 
%>

<header style="background-color: #333; color: white; padding: 10px 0; display: flex; justify-content: space-between; align-items: center;">
    <div style="margin-left: 20px;">
        <h1>IoTBay</h1>
    </div>
    <nav>
        <ul style="display: flex; list-style: none; margin-right: 20px;">
            <li style="margin-left: 20px;"><a href="index.jsp" style="color: white; text-decoration: none;">Home</a></li>
            <li style="margin-left: 20px;"><a href="products.jsp" style="color: white; text-decoration: none;">Products</a></li>
            
            <% if (headerUser != null) { %>
                <li style="margin-left: 20px;"><span style="color: white;">Welcome, <%= headerUser.getFirstName() %></span></li>
                <li style="margin-left: 20px;"><a href="LogoutServlet" style="color: white; text-decoration: none;">Logout</a></li>
                <li style="margin-left: 20px;"><a href="account.jsp" style="color: white; text-decoration: none;">My Account</a></li>
            <% } else { %>
                <li style="margin-left: 20px;"><a href="login.jsp" style="color: white; text-decoration: none;">Login</a></li>
                <li style="margin-left: 20px;"><a href="register.jsp" style="color: white; text-decoration: none;">Register</a></li>
            <% } %>
            
            <li style="margin-left: 20px;"><a href="cart.jsp" style="color: white; text-decoration: none;">Cart</a></li>
        </ul>
    </nav>
</header>
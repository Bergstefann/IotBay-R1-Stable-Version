<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Register - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .register-form-container {
                max-width: 600px;
                margin: 50px auto;
                padding: 40px;
                background: white;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }
            .register-form-container h2 {
                text-align: center;
                margin-bottom: 30px;
                font-size: 24px;
            }
            .form-row {
                display: flex;
                justify-content: space-between;
                margin-bottom: 15px;
            }
            .form-group {
                margin-bottom: 20px;
                text-align: left;
                flex: 0 0 48%;
            }
            .form-group.full-width {
                flex: 0 0 100%;
            }
            .form-group label {
                display: block;
                margin-bottom: 8px;
                font-weight: bold;
            }
            .form-group input {
                width: 100%;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 16px;
            }
            .checkbox-group {
                display: flex;
                align-items: center;
                margin-bottom: 20px;
            }
            .checkbox-group label {
                margin-right: 10px;
                font-weight: bold;
            }
            .checkbox-group input {
                margin-top: 2px;
            }
            .form-button {
                width: 100%;
                background-color: #F96E46;
                color: white;
                padding: 12px;
                font-size: 16px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-top: 10px;
                margin-bottom: 20px;
            }
            .form-button:hover {
                background-color: #e05e39;
            }
            .form-footer {
                margin-top: 20px;
                font-size: 14px;
                text-align: center;
            }
            .form-footer a {
                color: #0077cc;
                text-decoration: none;
            }
            .form-footer a:hover {
                text-decoration: underline;
            }
            .error-message {
                color: red;
                margin-bottom: 15px;
                text-align: center;
                font-weight: bold;
            }
            @media (max-width: 768px) {
                .form-row {
                    flex-direction: column;
                }
                .form-group {
                    flex: 0 0 100%;
                }
            }
        </style>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <main>
            <section class="register-form-container">
                <h2>Register</h2>
                <%
                    String errorMsg = (String) session.getAttribute("errorMsg");
                    if (errorMsg != null) {
                %>
                    <div class="error-message"><%= errorMsg %></div>
                <%
                        session.removeAttribute("errorMsg");
                    }
                %>
                <form action="RegisterServlet" method="post">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="firstName">First Name:</label>
                            <input type="text" id="firstName" name="firstName" required>
                        </div>
                       
                        <div class="form-group">
                            <label for="lastName">Last Name:</label>
                            <input type="text" id="lastName" name="lastName" required>
                        </div>
                    </div>
                   
                    <div class="form-group full-width">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                   
                    <div class="form-group full-width">
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                   
                    <div class="form-group full-width">
                        <label for="confirmPassword">Confirm Password:</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required>
                    </div>
                   
                    <div class="form-group full-width">
                        <label for="phoneNumber">Phone Number:</label>
                        <input type="tel" id="phoneNumber" name="phoneNumber">
                    </div>
                   
                    <div class="checkbox-group">
                        <label for="TOS">I agree to the Terms of Service:</label>
                        <input type="checkbox" id="TOS" name="TOS" required>
                    </div>
                   
                    <button type="submit" class="form-button">Register</button>
                   
                    <div class="form-footer">
                        <p>Already have an account? <a href="login.jsp">Log in</a></p>
                    </div>
                </form>
            </section>
        </main>
        <%@ include file="footer.jsp" %>
    </body>
</html>
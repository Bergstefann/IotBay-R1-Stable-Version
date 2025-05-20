<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .form-container {
                max-width: 500px;
                margin: 50px auto;
                padding: 40px;
                background: white;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            .form-container h2 {
                text-align: center;
                margin-bottom: 30px;
                font-size: 24px;
            }

            .form-group {
                margin-bottom: 20px;
                text-align: left;
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

            .btn-login {
                width: 100%;
                background-color: #d66b43;
                color: white;
                padding: 12px;
                font-size: 16px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-top: 10px;
            }

            .btn-login:hover {
                background-color: #c65c38;
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
        </style>
    </head>
    <body>
        <%@ include file="header.jsp" %>

        <main>
            <section class="form-container">
                <h2>Log In</h2>

                <% 
                    // Display error message if present
                    String errorMsg = (String) session.getAttribute("errorMsg");
                    if (errorMsg != null) {
                %>
                    <div class="error-message"><%= errorMsg %></div>
                <% 
                        // Clear the error message
                        session.removeAttribute("errorMsg");
                    } 
                %>

                <form action="LoginServlet" method="post">
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="text" id="email" name="email" required>
                    </div>

                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" required>
                    </div>

                    <button type="submit" class="btn-login">Login</button>
                </form>

                <div class="form-footer">
                    <p>Don't have an account? <a href="register.jsp">Register here</a></p>
                </div>
            </section>
        </main>

        <%@ include file="footer.jsp" %>
    </body>
</html>
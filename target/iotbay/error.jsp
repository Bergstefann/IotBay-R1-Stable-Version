<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Error - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .error-container {
                text-align: center;
                padding: 3rem;
                margin: 2rem auto;
                max-width: 600px;
                background-color: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }
            
            .error-icon {
                font-size: 4rem;
                color: #dc3545;
                margin-bottom: 1rem;
            }
            
            .error-title {
                font-size: 2rem;
                color: #333;
                margin-bottom: 1rem;
            }
            
            .error-message {
                color: #555;
                margin-bottom: 2rem;
                font-size: 1.1rem;
            }
            
            .home-button {
                display: inline-block;
                padding: 10px 20px;
                background-color: #F96E46;
                color: white;
                text-decoration: none;
                border-radius: 4px;
                transition: background-color 0.3s;
            }
            
            .home-button:hover {
                background-color: #e05e39;
            }
        </style>
    </head>

    <body>
        <%@ include file="header.jsp" %>

        <main>
            <div class="error-container">
                <div class="error-icon">⚠️</div>
                <h1 class="error-title">Oops! Something went wrong.</h1>
                
                <% 
                    // Get error message from request
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    if (errorMessage == null) {
                        errorMessage = "An unexpected error occurred.";
                    }
                %>
                
                <p class="error-message"><%= errorMessage %></p>
                
                <a href="index.jsp" class="home-button">Return to Home</a>
            </div>
        </main>

        <%@ include file="footer.jsp" %>
    </body>
</html>
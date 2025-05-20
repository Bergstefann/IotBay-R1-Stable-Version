<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>IoTBay - Home</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .hero-section {
                text-align: center;
                padding: 60px 20px;
                background-color: #f8f9fa;
                border-radius: 10px;
                margin-bottom: 30px;
            }
            
            .hero-section h1 {
                font-size: 2.5em;
                margin-bottom: 20px;
                color: #333;
            }
            
            .hero-section p {
                font-size: 1.2em;
                margin-bottom: 30px;
                color: #555;
                max-width: 800px;
                margin-left: auto;
                margin-right: auto;
            }
            
            .cta-buttons {
                margin-top: 30px;
            }
            
            .cta-button {
                display: inline-block;
                padding: 12px 24px;
                font-size: 16px;
                margin: 0 10px;
                background-color: #F96E46;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                text-decoration: none;
                transition: background-color 0.3s;
            }
            
            .cta-button:hover {
                background-color: #e05e39;
            }
            
            .cta-button.secondary {
                background-color: #6c757d;
            }
            
            .cta-button.secondary:hover {
                background-color: #5a6268;
            }
            
            .features-section {
                display: flex;
                flex-wrap: wrap;
                justify-content: space-between;
                margin: 40px 0;
            }
            
            .feature-card {
                flex: 0 0 30%;
                background-color: white;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 20px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                text-align: center;
            }
            
            .feature-card h3 {
                margin-top: 0;
                color: #333;
            }
            
            .feature-card p {
                color: #666;
            }
            
            @media (max-width: 768px) {
                .feature-card {
                    flex: 0 0 100%;
                }
            }
        </style>
    </head>

    <body>
        <%@ include file="header.jsp" %>
        

        <main>
            <section class="hero-section">
                <h1>Welcome to IoTBay!</h1>
                <p>Your one-stop shop for IoT devices and solutions. Discover the latest in smart home technology, security systems, and more.</p>

                <div class="cta-buttons">
                    <% if (headerUser == null) { %>
                        <a href="login.jsp" class="cta-button">Login</a>
                        <a href="register.jsp" class="cta-button secondary">Register</a>
                    <% } else { %>
                        <a href="products.jsp" class="cta-button">Browse Products</a>
                        <a href="account.jsp" class="cta-button secondary">My Account</a>
                    <% } %>
                </div>
            </section>
            
            <section class="features-section">
                <div class="feature-card">
                    <h3>Wide Selection</h3>
                    <p>Explore our extensive catalog of IoT devices from leading manufacturers.</p>
                </div>
                <div class="feature-card">
                    <h3>Expert Support</h3>
                    <p>Get assistance from our team of IoT specialists for all your needs.</p>
                </div>
                <div class="feature-card">
                    <h3>Fast Shipping</h3>
                    <p>Enjoy quick delivery to your doorstep with our efficient shipping service.</p>
                </div>
            </section>
        </main>

        <%@ include file="footer.jsp" %>
        
        <!-- Initialize database connection if not already done -->
        <jsp:include page="/ConnServlet" flush="true" />
    </body>
</html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@page import="model.Product"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IoTBay - Main Page</title>
    <link rel="stylesheet" href="css.css">
    <style>
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .user-welcome {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .user-welcome h2 {
            font-size: 28px;
            text-align: center;
            margin-bottom: 15px;
        }

        .user-welcome p {
            text-align: center;
            margin-bottom: 20px;
        }
        
        .product-grid {
            display: flex;
            flex-direction: column;
            margin-bottom: 40px;
        }
        
        .product-card {
            display: flex;
            margin-bottom: 30px;
            padding: 30px;
            border: 1px solid #ddd;
            border-radius: 4px;
            min-height: 400px;
        }
        
        .product-info {
            flex: 1;
            padding-right: 30px;
            max-width: 60%;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }
        
        .product-image {
            width: 400px;
            height: 400px;
            border: 1px solid #eee;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .product-card img {
            max-width: 100%;
            max-height: 100%;
        }
        
        .product-card h3 {
            font-size: 30px;
            margin-top: 0;
            margin-bottom: 30px;
        }
        
        .product-info p {
            font-size: 16px;
            line-height: 1.5;
            margin-bottom: 30px;
        }
        
        .price {
            font-size: 23px;
            font-weight: bold;
            color: #e03623;
            margin-bottom: 30px;
        }
        
        .section-title {
            text-align: center;
            margin: 40px 0 20px;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }
        
        .about-us {
            margin-bottom: 40px;
        }
        
        .newsletter-form {
            display: flex;
            max-width: 500px;
            margin: 0 auto;
        }
        
        .newsletter-form input[type="email"] {
            flex-grow: 1;
            padding: 8px;
            border: 1px solid #ddd;
        }
        
        .newsletter-form button {
            background-color: #ff6c44;
            color: white;
            border: none;
            padding: 8px 15px;
            cursor: pointer;
        }
        
        .button {
            background-color: #ff6c44;
            color: white;
            padding: 8px 15px;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
        }
    </style>
</head>
<body>
    <% 
        User user = (User)session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>
    
    <jsp:include page="header.jsp" />
    
    <div class="container">
        <div class="user-welcome">
            <h2>Welcome to IoTBay, <%= user.getFname() + " " + user.getLname() %>!</h2>
            <p>Your one-stop shop for all IoT devices and solutions.</p>
        </div>
        
        <h2 class="section-title">New Items</h2>
        <section class="product-grid">
            <%
                Product[] featuredProducts = new Product[3];
                
                featuredProducts[0] = new Product(1, "Smart Home Hub", "product1.jpg", 
                    "Connect and control all your smart home devices", 129.99, 50, false);
                
                featuredProducts[1] = new Product(2, "Security Camera", "product2.jpg", 
                    "HD security camera with motion detection", 89.99, 30, false);
                
                featuredProducts[2] = new Product(3, "Smart Thermostat", "product3.jpg", 
                    "Energy-saving smart thermostat", 79.99, 25, false);
                
                for(Product product : featuredProducts) {
            %>
            <div class="product-card">
                <div class="product-info">
                    <div>
                        <h3><%= product.getName() %></h3>
                        <p><%= product.getDescription() %></p>
                    </div>
                    <div>
                        <p class="price">$<%= String.format("%.2f", product.getPrice()) %></p>
                        <a href="product.jsp?id=<%= product.getProductID() %>" class="button">View Details</a>
                    </div>
                </div>
                <div class="product-image">
                    <img src="images/<%= product.getImageUrl() %>" alt="<%= product.getName() %>" 
                         onerror="this.src='images/placeholder.jpg'">
                </div>
            </div>
            <% } %>
        </section>
        
        <h2 class="section-title">About Us</h2>
        <section class="about-us">
            <p>IoTBay is your one-stop shop for IoT devices and solutions. We offer a wide range of products to help you build your smart home or business.</p>
            <p>Our mission is to provide high-quality IoT devices at competitive prices, with excellent customer service and technical support.</p>
        </section>
        
        <h2 class="section-title">Sign up for Newsletter</h2>
        <section class="newsletter">
            <form action="newsletter" method="post" class="newsletter-form">
                <input type="email" name="email" placeholder="Email@example.com" required>
                <button type="submit">Subscribe</button>
            </form>
        </section>
    </div>
    
    <jsp:include page="footer.jsp" />
</body>
</html>
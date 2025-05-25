<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@page import="model.Product"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Products - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .search-container {
                display: flex;
                justify-content: center;
                margin-bottom: 2rem;
            }
            
            .search-container form {
                display: flex;
                width: 100%;
                max-width: 600px;
            }
            
            .search-container input {
                flex: 1;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 4px 0 0 4px;
                font-size: 16px;
            }
            
            .search-container button {
                background-color: #F96E46;
                color: white;
                border: none;
                padding: 0 20px;
                border-radius: 0 4px 4px 0;
                cursor: pointer;
            }
            
            .category-filter {
                margin-bottom: 1.5rem;
                display: flex;
                justify-content: center;
                flex-wrap: wrap;
                gap: 10px;
            }
            
            .category-filter button {
                background-color: #f0f0f0;
                border: none;
                padding: 8px 15px;
                border-radius: 20px;
                cursor: pointer;
                transition: background-color 0.3s;
            }
            
            .category-filter button:hover, .category-filter button.active {
                background-color: #F96E46;
                color: white;
            }
            
            .no-products {
                text-align: center;
                padding: 2rem;
                background-color: #f9f9f9;
                border-radius: 8px;
                margin-top: 2rem;
            }
            
            .product-card .btn {
                width: 100%;
                text-align: center;
                margin-top: 10px;
            }
        </style>
    </head>

    <body>
        <%@ include file="header.jsp" %>

        <main>
            <h1>Products</h1>
            
            <div class="search-container">
                <form action="ProductServlet" method="get">
                    <input type="hidden" name="action" value="search">
                    <input type="text" name="query" placeholder="Search products..." 
                           value="${requestScope.searchQuery != null ? requestScope.searchQuery : ''}">
                    <button type="submit">Search</button>
                </form>
            </div>
            
            <div class="category-filter">
                <button class="active">All</button>
                <button>Smart Home</button>
                <button>Security</button>
                <button>Lighting</button>
                <button>Speakers</button>
                <button>Sensors</button>
            </div>
            
            <%
                List<Product> products = (List<Product>) request.getAttribute("products");
                
                if (products == null) {
                    response.sendRedirect("ProductServlet?action=list");
                    return;
                }
                
                if (products.isEmpty()) {
            %>
                <div class="no-products">
                    <h3>No products found</h3>
                    <% if (request.getAttribute("searchQuery") != null) { %>
                        <p>No products match your search query: '<%= request.getAttribute("searchQuery") %>'</p>
                        <p><a href="ProductServlet?action=list">View all products</a></p>
                    <% } else { %>
                        <p>There are currently no products available.</p>
                    <% } %>
                </div>
            <% } else { %>
                <div class="product-grid">
                    <% for (Product product : products) { %>
                        <div class="product-card">
                            <img src="resources/images/<%= product.getImageUrl() %>" alt="<%= product.getName() %>" 
                                 class="product-image" onerror="this.src='resources/images/product-placeholder.jpg'">
                            <div class="product-info">
                                <h3 class="product-title"><%= product.getName() %></h3>
                                <p class="product-price"><%= product.getFormattedPrice() %></p>
                                <p class="product-description"><%= product.getShortDescription(100) %></p>
                                <a href="ProductServlet?action=view&id=<%= product.getProductID() %>" class="btn">View Details</a>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </main>

        <%@ include file="footer.jsp" %>
        
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const filterButtons = document.querySelectorAll('.category-filter button');
                
                filterButtons.forEach(button => {
                    button.addEventListener('click', function() {
                        filterButtons.forEach(btn => btn.classList.remove('active'));
                        
                        this.classList.add('active');
                    });
                });
            });
        </script>
    </body>
</html>
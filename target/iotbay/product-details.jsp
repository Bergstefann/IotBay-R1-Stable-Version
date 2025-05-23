<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@page import="model.Product"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Product Details - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .product-container {
                display: flex;
                flex-wrap: wrap;
                margin: 2rem 0;
                background: white;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }
            
            .product-image-section {
                flex: 0 0 40%;
                padding: 2rem;
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: #f9f9f9;
            }
            
            .product-image {
                max-width: 100%;
                max-height: 400px;
                object-fit: contain;
            }
            
            .product-details {
                flex: 0 0 60%;
                padding: 2rem;
            }
            
            .product-title {
                font-size: 2rem;
                margin-bottom: 1rem;
                color: #333;
            }
            
            .product-price {
                font-size: 1.5rem;
                font-weight: bold;
                color: #F96E46;
                margin-bottom: 1.5rem;
            }
            
            .product-description {
                margin-bottom: 2rem;
                line-height: 1.6;
                color: #555;
            }
            
            .product-meta {
                margin-bottom: 2rem;
                padding-bottom: 1rem;
                border-bottom: 1px solid #eee;
            }
            
            .product-meta p {
                margin-bottom: 0.5rem;
                color: #666;
            }
            
            .product-meta .label {
                font-weight: bold;
                color: #333;
                display: inline-block;
                width: 120px;
            }
            
            .quantity-selector {
                display: flex;
                align-items: center;
                margin-bottom: 1.5rem;
            }
            
            .quantity-btn {
                width: 36px;
                height: 36px;
                background-color: #f0f0f0;
                border: none;
                font-size: 1.2rem;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            
            .quantity-btn:hover {
                background-color: #e0e0e0;
            }
            
            .quantity-input {
                width: 60px;
                height: 36px;
                text-align: center;
                font-size: 1rem;
                margin: 0 10px;
                border: 1px solid #ddd;
            }
            
            .add-to-cart {
                padding: 12px 24px;
                font-size: 1rem;
                background-color: #F96E46;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                margin-right: 1rem;
                transition: background-color 0.3s;
            }
            
            .add-to-cart:hover {
                background-color: #e05e39;
            }
            
            .wishlist-btn {
                padding: 12px 24px;
                font-size: 1rem;
                background-color: #f0f0f0;
                color: #333;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                transition: background-color 0.3s;
            }
            
            .wishlist-btn:hover {
                background-color: #e0e0e0;
            }
            
            .stock-status {
                display: inline-block;
                padding: 5px 10px;
                border-radius: 4px;
                font-size: 0.9rem;
                font-weight: bold;
                margin-bottom: 1.5rem;
            }
            
            .in-stock {
                background-color: #d4edda;
                color: #155724;
            }
            
            .out-of-stock {
                background-color: #f8d7da;
                color: #721c24;
            }
            
            @media (max-width: 992px) {
                .product-image-section, .product-details {
                    flex: 0 0 100%;
                }
            }
        </style>
    </head>
    <body>
        <%@ include file="header.jsp" %>

        <main>
            <%
                Product product = (Product) request.getAttribute("product");
                
                if (product == null) {
                    response.sendRedirect("ProductServlet?action=list");
                    return;
                }
            %>
            
            <div class="product-container">
                <div class="product-image-section">
                    <img src="resources/images/<%= product.getImageUrl() %>" alt="<%= product.getName() %>" 
                         class="product-image" onerror="this.src='resources/images/product-placeholder.jpg'">
                </div>
                
                <div class="product-details">
                    <h1 class="product-title"><%= product.getName() %></h1>
                    
                    <div class="product-price"><%= product.getFormattedPrice() %></div>
                    
                    <% if (product.getQuantity() > 0) { %>
                        <div class="stock-status in-stock">In Stock</div>
                    <% } else { %>
                        <div class="stock-status out-of-stock">Out of Stock</div>
                    <% } %>
                    
                    <div class="product-description">
                        <%= product.getDescription() %>
                    </div>
                    
                    <div class="product-meta">
                        <p><span class="label">Product ID:</span> <%= product.getProductID() %></p>
                        <p><span class="label">Availability:</span> <%= product.getQuantity() %> units</p>
                    </div>
                    
                    <form action="CartServlet" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="<%= product.getProductID() %>">
                        
                        <div class="quantity-selector">
                            <span class="label">Quantity:</span>
                            <button type="button" class="quantity-btn minus">-</button>
                            <input type="number" name="quantity" value="1" min="1" max="<%= product.getQuantity() %>" class="quantity-input">
                            <button type="button" class="quantity-btn plus">+</button>
                        </div>
                        
                        <div class="action-buttons">
                            <button type="submit" class="add-to-cart" <%= product.getQuantity() == 0 ? "disabled" : "" %>>
                                Add to Cart
                            </button>
                            
                            <button type="button" class="wishlist-btn">
                                Add to Wishlist
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            
            <div class="back-link">
                <a href="ProductServlet?action=list">&larr; Back to Products</a>
            </div>
        </main>

        <%@ include file="footer.jsp" %>
        
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const minusBtn = document.querySelector('.quantity-btn.minus');
                const plusBtn = document.querySelector('.quantity-btn.plus');
                const quantityInput = document.querySelector('.quantity-input');
                const maxQuantity = <%= product.getQuantity() %>;
                
                minusBtn.addEventListener('click', function() {
                    let currentValue = parseInt(quantityInput.value);
                    if (currentValue > 1) {
                        quantityInput.value = currentValue - 1;
                    }
                });
                
                plusBtn.addEventListener('click', function() {
                    let currentValue = parseInt(quantityInput.value);
                    if (currentValue < maxQuantity) {
                        quantityInput.value = currentValue + 1;
                    }
                });
                
                quantityInput.addEventListener('change', function() {
                    let currentValue = parseInt(quantityInput.value);
                    
                    if (isNaN(currentValue) || currentValue < 1) {
                        quantityInput.value = 1;
                    } else if (currentValue > maxQuantity) {
                        quantityInput.value = maxQuantity;
                    }
                });
            });
        </script>
    </body>
</html>
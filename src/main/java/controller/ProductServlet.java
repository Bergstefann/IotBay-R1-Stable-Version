package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Product;
import model.dao.ProductDAO;

public class ProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        ProductDAO productManager = (ProductDAO) session.getAttribute("productManager");
        
        if (productManager == null) {
            LOGGER.log(Level.SEVERE, "ProductDAO not found in session");
            request.setAttribute("errorMessage", "System error: Database connection not available.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        try {
            switch (action) {
                case "list":
                    List<Product> products = productManager.getAllProducts();
                    request.setAttribute("products", products);
                    request.getRequestDispatcher("products.jsp").forward(request, response);
                    break;
                    
                case "view":
                    String productIdStr = request.getParameter("id");
                    if (productIdStr == null || productIdStr.isEmpty()) {
                        response.sendRedirect("ProductServlet?action=list");
                        return;
                    }
                    
                    try {
                        int productId = Integer.parseInt(productIdStr);
                        Product product = productManager.getProductById(productId);
                        
                        if (product == null) {
                            request.setAttribute("errorMessage", "Product not found.");
                            request.getRequestDispatcher("error.jsp").forward(request, response);
                            return;
                        }
                        
                        request.setAttribute("product", product);
                        request.getRequestDispatcher("product-details.jsp").forward(request, response);
                        
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.WARNING, "Invalid product ID: " + productIdStr, e);
                        response.sendRedirect("ProductServlet?action=list");
                    }
                    break;
                    
                case "search":
                    String query = request.getParameter("query");
                    if (query == null || query.isEmpty()) {
                        response.sendRedirect("ProductServlet?action=list");
                        return;
                    }
                    
                    List<Product> searchResults = productManager.searchProductsByName(query);
                    request.setAttribute("products", searchResults);
                    request.setAttribute("searchQuery", query);
                    request.getRequestDispatcher("products.jsp").forward(request, response);
                    break;
                    
                default:
                    response.sendRedirect("ProductServlet?action=list");
                    break;
            }
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error in ProductServlet", ex);
            request.setAttribute("errorMessage", "A database error occurred. Please try again later.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
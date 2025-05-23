package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import model.dao.DBConnector;
import model.dao.UserDAO;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            UserDAO userDAO = (UserDAO) session.getAttribute("manager");
            
            if (userDAO == null) {
                userDAO = (UserDAO) getServletContext().getAttribute("userManager");
                
                if (userDAO == null) {
                    System.out.println("UserDAO not found in session or servlet context, creating new connection");
                    DBConnector db = new DBConnector();
                    Connection conn = db.openConnection();
                    userDAO = new UserDAO(conn);
                    
                    session.setAttribute("manager", userDAO);
                    getServletContext().setAttribute("userManager", userDAO);
                } else {
                    System.out.println("UserDAO found in servlet context");
                    session.setAttribute("manager", userDAO);
                }
            } else {
                System.out.println("UserDAO found in session");
            }
            
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            System.out.println("Attempting login for email: " + email);
            
            try {
                User user = userDAO.findUser(email, password);
                
                if (user != null) {
                    System.out.println("Login successful for: " + email);
                    session.setAttribute("user", user);
                    response.sendRedirect("index.jsp");
                } else {
                    System.out.println("Login failed for: " + email);
                    session.setAttribute("errorMsg", "Invalid email or password. Please try again.");
                    response.sendRedirect("login.jsp");
                }
            } catch (SQLException e) {
                System.err.println("Database error during login: " + e.getMessage());
                e.printStackTrace();
                session.setAttribute("errorMsg", "Database error: " + e.getMessage());
                response.sendRedirect("login.jsp");
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            
            session.setAttribute("errorMsg", "System error: Database connection not available");
            response.sendRedirect("login.jsp");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {
    
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
            
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String phoneNumber = request.getParameter("phoneNumber");
            String tosAgreed = request.getParameter("TOS");
            
            System.out.println("Registration attempt with:");
            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
            System.out.println("Email: " + email);
            System.out.println("Phone Number: " + phoneNumber);
            System.out.println("TOS agreed: " + tosAgreed);
            
            if (firstName == null || lastName == null || email == null || password == null) {
                session.setAttribute("errorMsg", "All fields are required");
                response.sendRedirect("register.jsp");
                return;
            }
            
            if (password == null || (confirmPassword != null && !password.equals(confirmPassword))) {
                session.setAttribute("errorMsg", "Passwords do not match");
                response.sendRedirect("register.jsp");
                return;
            }
            
            if (tosAgreed == null) {
                session.setAttribute("errorMsg", "You must agree to the Terms of Service");
                response.sendRedirect("register.jsp");
                return;
            }
            
            try {
                try {
                    Connection conn = userDAO.getConnection();
                    String sql = "INSERT INTO User (firstName, lastName, email, password, phoneNumber, role) VALUES (?, ?, ?, ?, ?, ?)";
                    
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, firstName);
                        stmt.setString(2, lastName);
                        stmt.setString(3, email);
                        stmt.setString(4, password);
                        stmt.setString(5, phoneNumber);
                        stmt.setString(6, "customer");
                        
                        System.out.println("Executing SQL: " + sql);
                        int rowsAffected = stmt.executeUpdate();
                        
                        if (rowsAffected > 0) {
                            System.out.println("User registered successfully");
                            session.setAttribute("errorMsg", "Registration successful! Please login.");
                            response.sendRedirect("login.jsp");
                            return;
                        } else {
                            System.out.println("User registration failed - no rows affected");
                            session.setAttribute("errorMsg", "Registration failed. Please try again.");
                            response.sendRedirect("register.jsp");
                            return;
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error with direct SQL: " + e.getMessage());
                    
                    User user = new User();
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setPhone(phoneNumber);
                    user.setRole("customer");
                    
                    boolean success = userDAO.createUser(user);
                    
                    if (success) {
                        System.out.println("User registered successfully via User object");
                        session.setAttribute("errorMsg", "Registration successful! Please login.");
                        response.sendRedirect("login.jsp");
                        return;
                    } else {
                        System.out.println("User registration failed via User object");
                        session.setAttribute("errorMsg", "Registration failed. Please try again.");
                        response.sendRedirect("register.jsp");
                        return;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Database error during registration: " + e.getMessage());
                e.printStackTrace();
                session.setAttribute("errorMsg", "Database error: " + e.getMessage());
                response.sendRedirect("register.jsp");
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            
            session.setAttribute("errorMsg", "System error: Database connection not available");
            response.sendRedirect("register.jsp");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}
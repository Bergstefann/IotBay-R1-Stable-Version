/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: jetty/9.4.46.v20220331
 * Generated at: 2025-05-24 01:50:54 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import model.User;

public final class login_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(2);
    _jspx_dependants.put("/footer.jsp", Long.valueOf(1747653806114L));
    _jspx_dependants.put("/header.jsp", Long.valueOf(1747967870271L));
  }

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("model.User");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    final java.lang.String _jspx_method = request.getMethod();
    if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS");
      return;
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html>\r\n");
      out.write("    <head>\r\n");
      out.write("        <meta charset=\"UTF-8\">\r\n");
      out.write("        <title>Login - IoTBay</title>\r\n");
      out.write("        <link href=\"css.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
      out.write("        <style>\r\n");
      out.write("            .form-container {\r\n");
      out.write("                max-width: 500px;\r\n");
      out.write("                margin: 50px auto;\r\n");
      out.write("                padding: 40px;\r\n");
      out.write("                background: white;\r\n");
      out.write("                border-radius: 10px;\r\n");
      out.write("                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            .form-container h2 {\r\n");
      out.write("                text-align: center;\r\n");
      out.write("                margin-bottom: 30px;\r\n");
      out.write("                font-size: 24px;\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            .form-group {\r\n");
      out.write("                margin-bottom: 20px;\r\n");
      out.write("                text-align: left;\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            .form-group label {\r\n");
      out.write("                display: block;\r\n");
      out.write("                margin-bottom: 8px;\r\n");
      out.write("                font-weight: bold;\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            .form-group input {\r\n");
      out.write("                width: 100%;\r\n");
      out.write("                padding: 10px;\r\n");
      out.write("                border: 1px solid #ccc;\r\n");
      out.write("                border-radius: 5px;\r\n");
      out.write("                font-size: 16px;\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            .btn-login {\r\n");
      out.write("                width: 100%;\r\n");
      out.write("                background-color: #d66b43;\r\n");
      out.write("                color: white;\r\n");
      out.write("                padding: 12px;\r\n");
      out.write("                font-size: 16px;\r\n");
      out.write("                border: none;\r\n");
      out.write("                border-radius: 5px;\r\n");
      out.write("                cursor: pointer;\r\n");
      out.write("                margin-top: 10px;\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            .btn-login:hover {\r\n");
      out.write("                background-color: #c65c38;\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            .form-footer {\r\n");
      out.write("                margin-top: 20px;\r\n");
      out.write("                font-size: 14px;\r\n");
      out.write("                text-align: center;\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            .form-footer a {\r\n");
      out.write("                color: #0077cc;\r\n");
      out.write("                text-decoration: none;\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            .form-footer a:hover {\r\n");
      out.write("                text-decoration: underline;\r\n");
      out.write("            }\r\n");
      out.write("\r\n");
      out.write("            .error-message {\r\n");
      out.write("                color: red;\r\n");
      out.write("                margin-bottom: 15px;\r\n");
      out.write("                text-align: center;\r\n");
      out.write("                font-weight: bold;\r\n");
      out.write("            }\r\n");
      out.write("        </style>\r\n");
      out.write("    </head>\r\n");
      out.write("    <body>\r\n");
      out.write("        ");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
 
    User headerUser = (User) session.getAttribute("user"); 

      out.write("\r\n");
      out.write("\r\n");
      out.write("<header style=\"background-color: #333; color: white; padding: 10px 0; display: flex; justify-content: space-between; align-items: center;\">\r\n");
      out.write("    <div style=\"margin-left: 20px;\">\r\n");
      out.write("        <h1>IoTBay</h1>\r\n");
      out.write("    </div>\r\n");
      out.write("    <nav>\r\n");
      out.write("        <ul style=\"display: flex; list-style: none; margin-right: 20px;\">\r\n");
      out.write("            <li style=\"margin-left: 20px;\"><a href=\"index.jsp\" style=\"color: white; text-decoration: none;\">Home</a></li>\r\n");
      out.write("            <li style=\"margin-left: 20px;\"><a href=\"products.jsp\" style=\"color: white; text-decoration: none;\">Products</a></li>\r\n");
      out.write("            \r\n");
      out.write("            ");
 if (headerUser != null) { 
      out.write("\r\n");
      out.write("                <li style=\"margin-left: 20px;\"><span style=\"color: white;\">Welcome, ");
      out.print( headerUser.getFirstName() );
      out.write("</span></li>\r\n");
      out.write("                <li style=\"margin-left: 20px;\"><a href=\"LogoutServlet\" style=\"color: white; text-decoration: none;\">Logout</a></li>\r\n");
      out.write("                <li style=\"margin-left: 20px;\"><a href=\"account.jsp\" style=\"color: white; text-decoration: none;\">My Account</a></li>\r\n");
      out.write("            ");
 } else { 
      out.write("\r\n");
      out.write("                <li style=\"margin-left: 20px;\"><a href=\"login.jsp\" style=\"color: white; text-decoration: none;\">Login</a></li>\r\n");
      out.write("                <li style=\"margin-left: 20px;\"><a href=\"register.jsp\" style=\"color: white; text-decoration: none;\">Register</a></li>\r\n");
      out.write("            ");
 } 
      out.write("\r\n");
      out.write("            \r\n");
      out.write("            <li style=\"margin-left: 20px;\"><a href=\"cart.jsp\" style=\"color: white; text-decoration: none;\">Cart</a></li>\r\n");
      out.write("        </ul>\r\n");
      out.write("    </nav>\r\n");
      out.write("</header>");
      out.write("\r\n");
      out.write("\r\n");
      out.write("        <main>\r\n");
      out.write("            <section class=\"form-container\">\r\n");
      out.write("                <h2>Log In</h2>\r\n");
      out.write("\r\n");
      out.write("                ");
 
                    String errorMsg = (String) session.getAttribute("errorMsg");
                    if (errorMsg != null) {
                
      out.write("\r\n");
      out.write("                    <div class=\"error-message\">");
      out.print( errorMsg );
      out.write("</div>\r\n");
      out.write("                ");
 
                        session.removeAttribute("errorMsg");
                    } 
                
      out.write("\r\n");
      out.write("\r\n");
      out.write("                <form action=\"LoginServlet\" method=\"post\">\r\n");
      out.write("                    <div class=\"form-group\">\r\n");
      out.write("                        <label for=\"email\">Email:</label>\r\n");
      out.write("                        <input type=\"text\" id=\"email\" name=\"email\" required>\r\n");
      out.write("                    </div>\r\n");
      out.write("\r\n");
      out.write("                    <div class=\"form-group\">\r\n");
      out.write("                        <label for=\"password\">Password:</label>\r\n");
      out.write("                        <input type=\"password\" id=\"password\" name=\"password\" required>\r\n");
      out.write("                    </div>\r\n");
      out.write("\r\n");
      out.write("                    <button type=\"submit\" class=\"btn-login\">Login</button>\r\n");
      out.write("                </form>\r\n");
      out.write("\r\n");
      out.write("                <div class=\"form-footer\">\r\n");
      out.write("                    <p>Don't have an account? <a href=\"register.jsp\">Register here</a></p>\r\n");
      out.write("                </div>\r\n");
      out.write("            </section>\r\n");
      out.write("        </main>\r\n");
      out.write("\r\n");
      out.write("        ");
      out.write("\n");
      out.write("<footer>\n");
      out.write("    <div class=\"footer-content\">\n");
      out.write("        &copy; 2025 IoTBay. All rights reserved. | <a href=\"https://github.com/xJessD/iotbay\">GitHub Project Repo</a>             \n");
      out.write("    </div>\n");
      out.write("</footer>");
      out.write("\r\n");
      out.write("    </body>\r\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

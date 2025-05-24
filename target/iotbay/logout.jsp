<html>
    <% 
        // Variables go here
    %>

    <header>
        <title>Log Out</title>
        <link rel="stylesheet" href="css.css">
         
    </header>

    <body>
            <%@ include file="header.jsp" %>

        
        <main>
            <section>
                <%session.invalidate() ;%>

                <p>You are now logged out. Click here to go to <a href="login.jsp">home</a>.</p>
            </section>
                
        </main>
        

            <%@ include file="footer.jsp" %>
    </body>
</html>

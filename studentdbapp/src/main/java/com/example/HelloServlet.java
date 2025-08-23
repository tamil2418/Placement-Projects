package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    // @Override
    // protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    //         throws ServletException, IOException {
    //     resp.setContentType("text/html;charset=UTF-8");
    //     resp.getWriter().println("<h1>Hello from Servlet (javax)!</h1>");
    // }
     protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("[HelloServlet] doGet called"); // shows in Tomcat logs
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println("<h1>Hello from Servlet (javax)!</h1>");
        resp.getWriter().println("<p>ContextPath: " + req.getContextPath() + "</p>");
    }
}

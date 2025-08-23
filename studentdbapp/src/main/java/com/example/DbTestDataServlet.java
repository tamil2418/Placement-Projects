package com.example;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/dbtestdata")
public class DbTestDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
                try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/todo";
        String user = "root";      // 👈 change to your username
        String pass = "root";  // 👈 change to your password

        try (
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM todolist")
        ) {
            while (rs.next()) {
                resp.getWriter().println("User: " + rs.getString("author_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace(resp.getWriter());
        }
    }
}

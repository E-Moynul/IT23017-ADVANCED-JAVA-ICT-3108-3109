import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/guestbook")
public class GuestbookServlet extends HttpServlet {


    String url = "jdbc:mysql://localhost:3306/tiny_project";
    String user = "root";
    String pass = "";


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String[]> messages = new ArrayList<>();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, message FROM guestbook ORDER BY id DESC");

            while (rs.next()) {
                String[] entry = new String[2];
                entry[0] = rs.getString("name");
                entry[1] = rs.getString("message");
                messages.add(entry);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("messages", messages);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("username");
        String msg = request.getParameter("message");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            
            PreparedStatement ps = con.prepareStatement("INSERT INTO guestbook (name, message) VALUES (?, ?)");
            ps.setString(1, name);
            ps.setString(2, msg);
            ps.executeUpdate();
            
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("guestbook");
    }
}

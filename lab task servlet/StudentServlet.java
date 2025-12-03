import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/student")
public class StudentServlet extends HttpServlet {

    static List<String[]> studentDb = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        if ("Insert".equals(action)) {
            studentDb.add(new String[]{name, email});
        } 
        else if ("Delete".equals(action)) {
            studentDb.removeIf(s -> s[0].equals(name));
        }
        else if ("Update".equals(action)) {
            for (String[] s : studentDb) {
                if (s[0].equals(name)) {
                    s[1] = email;
                }
            }
        }

        request.setAttribute("currentAction", action);
        request.setAttribute("data", studentDb);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}

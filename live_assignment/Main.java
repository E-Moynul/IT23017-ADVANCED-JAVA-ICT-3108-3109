import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectJDBC {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test_db"; 
        String username = "root";
        String password = ""; 

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            
            if (con != null) {
                System.out.println("Successfully connected to the database!");
            }

            String insertQuery = "INSERT INTO student (name) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(insertQuery);
            pstmt.setString(1, "John Doe");
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " row(s) inserted.");

            String selectQuery = "SELECT * FROM student";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            System.out.println("\n--- Student Table Data ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                
                System.out.println("ID: " + id + ", Name: " + name);
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

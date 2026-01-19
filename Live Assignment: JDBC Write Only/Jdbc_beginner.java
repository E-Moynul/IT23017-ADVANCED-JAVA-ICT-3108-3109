
package com.mycompany.jdbc_beginner ;
import java.sql.* ;


public class Jdbc_beginner {

    public static void main(String[] args) {
        
        String url      = "jdbc:mysql://localhost:3306/testdb" ;
        String user     = "root" ;
        String password = "it23017@mbstu" ;
        
        
        
        
        try
        {
            
            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM info");
            
            
            while (rs.next())
            {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
            }
            
            con.close() ;
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        
        
    }
}

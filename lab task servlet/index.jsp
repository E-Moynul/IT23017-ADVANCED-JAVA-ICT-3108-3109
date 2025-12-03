<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Student Details</title>
        <style>
            body { font-family: sans-serif; padding: 20px; }
            input[type="text"] { margin-bottom: 10px; width: 200px; display: block; }
            table { border-collapse: collapse; width: 100%; margin-top: 20px; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
            .btn { padding: 5px 15px; margin-right: 5px; cursor: pointer; }
        </style>
    </head>
    <body>
        <h2>Enter Student Details</h2>
        <form action="student" method="post">
            <label>Name: </label>
            <input type="text" name="name" required>
            
            <label>Email: </label>
            <input type="text" name="email" required>
            
            <input type="submit" name="action" value="Insert" class="btn">
            <input type="submit" name="action" value="View" class="btn">
            <input type="submit" name="action" value="Update" class="btn">
            <input type="submit" name="action" value="Delete" class="btn">
        </form>

        <% 
            String currentAction = (String) request.getAttribute("currentAction");
            List<String[]> list = (List<String[]>) request.getAttribute("data");
            
            if ("View".equals(currentAction) && list != null) {
        %>
            <h3>Action: View</h3>
            <table>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                </tr>
                <% for (String[] s : list) { %>
                <tr>
                    <td><%= s[0] %></td>
                    <td><%= s[1] %></td>
                </tr>
                <% } %>
            </table>
        <% } %>
    </body>
</html>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tiny Guestbook</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        .entry { border-bottom: 1px solid #ccc; padding: 5px; }
        form { background: #f4f4f4; padding: 15px; margin-bottom: 20px; }
    </style>
</head>
<body>
    <h2>Leave a Message</h2>
    <form action="guestbook" method="post">
        <input type="text" name="username" placeholder="Your Name" required><br><br>
        <textarea name="message" placeholder="Your Message" required></textarea><br><br>
        <button type="submit">Post</button>
    </form>

    <h3>Messages:</h3>
    <%
        List<String[]> messages = (List<String[]>) request.getAttribute("messages");
        if (messages != null) {
            for (String[] entry : messages) {
    %>
        <div class="entry">
            <strong><%= entry[0] %>:</strong> <%= entry[1] %>
        </div>
    <%
            }
        } else {
    %>
        <p>No messages yet. <a href="guestbook">Click here to load.</a></p>
    <%
        }
    %>
</body>
</html>

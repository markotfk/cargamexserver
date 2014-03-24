<%-- 
    Document   : index
    Created on : Mar 17, 2014, 7:32:49 PM
    Author     : Marko Karjalainen <markotfk@gmail.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Car Game X - Internet car game</title>
        <script src="js/jquery-1.11.0.js"></script>
        <script src="js/main.js"></script>
        <script src="js/login.js"></script>
        <link rel="stylesheet" type="text/css" href="css/style.css">
    </head>
    
    <body>
        <div class="content">
            Welcome to Car Game X - the Internet car game. Please login:
            <form method="post" id="login_form" class="player_form">
                Login: <input type="text" name="login" id="login"><br>
                Password: <input type="password" name="password" id="password"><br>
                <input type="submit" value="Login user">
            </form> 

            <div class="errormsg" id="login_status"></div>
            <a href="register.html">Register New Player</a>
        </div>
    </body>
    
</html>

<%-- 
    Document   : index
    Created on : Mar 17, 2014, 7:32:49 PM
    Author     : Marko Karjalainen <markotfk@gmail.com>
--%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Car Game X - Internet car game</title>
        <script src="js/jquery/jquery-1.11.0.js"></script>
        <script src="js/main.js"></script>
        <script src="js/mainforms.js"></script>
        <link rel="stylesheet" type="text/css" href="css/style.css">
    </head>
    
    <body>
        <div class="content">
            Welcome to Car Game X - the Internet car game.
            <p>
            <div id="login_name" class="player_info"></div>
            <div id="login_created" class="player_info"></div>
            </p>
            <p>
                <form method="post" id="login_form" class="player_form">
                    Login: <input type="text" name="login" id="login"><br>
                    Password: <input type="password" name="password" id="password"><br>
                    <input type="submit" value="Login user">
                </form> 

                <form method="post" id="logout_form" class="player_form">
                    <input type="submit" value="Logout user">
                </form> 
            
                <form method="post" id="player_form_remove" class="player_form">
                    Remove Account<br>
                    <input type="submit" value="Remove Account">
                </form>
            </p>
            <p>
                <form method="post" id="team_form_add" class="player_form">
                    Create a Team:<br>
                    Name: <input type="text" name="Team Name" id="team_name"><br>
                    Description: <input type="text" name="Team Description" id="team_description"><br>
                    <input type="submit" value="Create Team">
                </form>
                <form method="post" id="team_form_remove" class="player_form">
                    Remove Team<br>
                    <input type="submit" value="Remove Team">
                </form>
            </p>
            
            <p>
                <div class="errormsg" id="error_status"></div>
            </p>
            <p>
                <div class="player_info" id="team_membership"></div>
                <div class="player_info" id="team_created"></div>
            </p>
            <p>
                <a href="register.html">Register New Player</a>
            </p>
        </div>
    </body>
    
</html>

<%-- 
    Document   : login
    Created on : Dec 9, 2017, 11:05:12 AM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
        <link rel="stylesheet" href="/styles/css/bootstrap.min.css">
        <link rel="stylesheet" href="/styles/css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="/styles/main.css">
        <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>

        <title>WeTrade</title>
    </head>
    <body class="stockTickerBG">
        <div class="container loginMarginTop" style>
            <div class="row">
                <div class="col">

                </div>
                <div class="col">
                    <a href="/about.html"> <img src='/styles/img/wetradeName-12.svg'/></a>
                    <form action="j_security_check" method="POST" class="inline"> 
                        <div class ="form-group">

                            <input type="text" placeholder= "email" class="form-control text-center " name="j_username">
                        </div>
                        <div class ="form-group">

                            <input type="password" placeholder="password" class="form-control text-center" name="j_password">
                        </div>

                        <Button type="submit" class="btn btn-success center-block w-100">Login</button> </form> 
                        <p class='text-center oneEmTopMargin'> Don't have an account yet? <a href="/marketSim?action=signup">Sign Up!</a></p>
                </div>
                <div class="col">

                </div>
            </div>
        </div>
    </body>
</html>

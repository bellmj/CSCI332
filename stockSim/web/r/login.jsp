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
        <link rel="stylesheet" href="/styles/main.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
        <link rel="stylesheet" href="/styles/css/bootstrap.min.css">
        <link rel="stylesheet" href="/styles/css/bootstrap-theme.min.css">
        <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
        
        <title>Sign Up</title>
    </head>
    <body>
        <nav class="navbar navbar-toggleable-sm navbar-light bg-faded">
            <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <a class="navbar-brand" href="#">StockSimulator2000</a>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                 
                   

                </ul>
            </div>
        </nav>
        <div class="container marginTop" style>
            <div class="row">
                <div class="col">
                
                </div>
                <div class="col">
                    <p>Please enter your email and password to continue.</p> 
                    <form action="j_security_check" method="get" class="inline"> 
                        <div class ="form-group">
                            <label>Email</label> 
                            <input type="text" class="form-control" name="j_username">
                        </div>
                        <div class ="form-group">
                            <label>Password</label> 
                            <input type="password" class="form-control" name="j_password">
                        </div>

                        <Button type="submit" class="btn btn-primary">Login</button> </form> 
                </div>
                <div class="col">
                
                </div>
            </div>
        </div>
    </body>
</html>

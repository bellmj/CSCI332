<%-- 
    Document   : signup
    Created on : Nov 17, 2017, 11:30:11 AM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
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
            <a class="navbar-brand" href="/about.html">StockSimulator2000</a>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                 
                   

                </ul>
            </div>
        </nav>
        <div class="container marginTop">
            <div class="row">
                <div class="col">

                </div>
                <div class="col">
                    <form action="<c:url value='/marketSim'/>" 
                          method="post" class="inline">
                        <div class="form-group">
                            <input type="hidden"  name="action" value="signup">
                            <label>Full Name</label>
                            <input type="text" name="name"
                                   value="${user.name}" class="form-control" required><span style="color:#f00">${nameErrorMessage}</span>
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" name="email"
                                   value="${user.email}" class="form-control" required><span style="color:#f00">${emailErrorMessage}</span>
                        </div>
                        <div class="form-group">
                            <label>Password</label>
                            <input type="password" name="password" 
                                   value="" class="form-control" required><span style="color:#f00">${passwordErrorMessage}</span>
                        </div>
                        <div class="form-group">
                            <label>Password Confirm</label>
                            <input type="password" name="passwordConfirm" 
                                   value="" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Initial Account Balance (max:$1,000,000)</label>
                            <input type="number" name="initialBalance" min="0.00" 
                                   max="1000000.00" step="0.01" class="form-control" value="${user.accountBalance}" />
                        </div>
                        <button type="submit" class="btn btn-primary">Sign Up</button>
                    </form>
                </div>
                <div class="col"></div>
            </div>
    </body>
</html>

<%-- 
    Document   : profile
    Created on : Nov 19, 2017, 2:46:19 AM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
        <link rel="stylesheet" href="/styles/css/bootstrap.min.css">
        <link rel="stylesheet" href="/styles/css/bootstrap-theme.min.css">
        <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
        <script src="styles/js/bootstrap.min.js"></script>
        <title>StockSimulator2000</title>
    </head>
    <body>
        <nav class="navbar navbar-toggleable-sm navbar-light bg-faded">
            <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <a class="navbar-brand" href="/about.html">StockSimulator2000</a>

            <div class="collapse navbar-collapse navbar-left" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">

                    <li class="nav-item active">
                        <a class="nav-link" href="<c:url value='/marketSim?action=stockQuoteHome'/>">Get Stock Quote</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="<c:url value='/marketSim?action=viewUserHoldings'/>">View My Stocks</a>
                    </li>

                </ul>
                <ul class="navbar-nav mr-auto navbar-right">
                    <li class="nav-item active">
                        <a class="nav-link" href="<c:url value='/logout.jsp'/>">logout</a>
                    </li>
                </ul>
            </div>
        </nav>
        <div class="container marginTop">
            <p>Welcome ${user.name}<a href="<c:url value='/marketSim?action=editUserName'/>"> <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></p>
            <p>Your current account balance is $${user.accountBalance}
                <a href="<c:url value='/marketSim?action=editAccountBalance'/>"> <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></p>
<!--            <a href="<c:url value='/marketSim?action=changePassword'/>">Change Password</a>-->
<!--            <br>-->
            <a href="<c:url value='${adminHomeLink}'/>">${adminHomeText}</a>
        </div>
    </body>
</html>

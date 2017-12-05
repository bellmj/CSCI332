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
        <link rel="stylesheet" href="/styles/css/">
        <script src="/styles/js/"></script>
        <title>StockSimulator2000</title>
    </head>
    <body>
        <nav class="navbar navbar-toggleable-sm navbar-light bg-faded">
            <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <a class="navbar-brand" href="#">StockSimulator2000</a>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="<c:url value='/marketSim?action=stockQuoteHome'/>">Get Stock Quote</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="<c:url value='/marketSim?action=viewUserHoldings'/>">View My Stocks</a>
                    </li>

                </ul>
            </div>
        </nav>
        <div class="container marginTop">
            <p>Welcome ${user.name}<a href="<c:url value='/marketSim?action=editUserName'/>"> <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></p>
            <p>Your current account balance is $${user.accountBalance}
                <a href="<c:url value='/marketSim?action=editAccountBalance'/>"> <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></p>
            <a href="<c:url value='/marketSim?action=changePassword'/>">Change Password</a>
            <p>Envelope icon: <span class="glyphicon glyphicon-envelope"></span></p> 
            <br>
            <a href="<c:url value='${adminHomeLink}'/>">${adminHomeText}</a>
        </div>
    </body>
</html>

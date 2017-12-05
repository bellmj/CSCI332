<%-- 
    Document   : editUserName
    Created on : Dec 3, 2017, 4:03:33 PM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body>
        <nav class="navbar navbar-toggleable-md navbar-light bg-faded">
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
        <form action="<c:url value='/marketSim'/>" 
              method="post" class="inline">
            <input type="hidden" name="action" value="editUserName">
            <label>Full Name</label>
            <input type="text" name="name"
                   value="${user.name}" required>
            <label>&nbsp;</label>
            <input type="submit" value="Save Name">
        </form>
        <p>Your current account balance is $${user.accountBalance}</p><a href="<c:url value='/marektSim?action=editAccountBalance'/>">Edit</a>
    </body>
</html>

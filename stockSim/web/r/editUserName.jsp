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
        <a href="<c:url value='/marketSim?action=stockQuoteHome'/>">Get Stock Quote</a>
        <br>
        <a href="<c:url value='/marketSim?action=viewUserHoldings'/>">View My Stocks</a>
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

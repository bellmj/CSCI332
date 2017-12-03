<%-- 
    Document   : editAccountBalance
    Created on : Dec 3, 2017, 4:17:04 PM
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
        <p>Welcome ${user.name}</p><a href="<c:url value='/marketSim?action=editUserName'/>">Edit</a>
        <br>
        <form action="<c:url value='/marketSim'/>" 
              method="post" class="inline">
            <input type="hidden" name="action" value="editUserAccountBalance">
            <label>New Account Balance</label>
           <input type="number" name="accountBalance" min="0.00" 
                   max="1000000.00" step="0.01" value="${user.accountBalance}" required>
            <label>&nbsp;</label>
            <input type="submit" value="Save Account Balance">
        </form>
    </body>
</html>
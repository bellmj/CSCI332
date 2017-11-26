<%-- 
    Document   : stockQuote
    Created on : Nov 25, 2017, 1:43:04 PM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Stock Quote</title>
    </head>
    <body>
        <p>Latest Stock Price For ${stockPrice.symbol} is <fmt:formatNumber value="${stockPrice.close}" type="currency"/></p>
        <p>You currently have <fmt:formatNumber value="${user.accountBalance}" type="currency"/> in your account</p>
        <p>Would you like to buy some shares?</p>
        <form action="<c:url value='/marketSim'/>" 
              method="post" class="inline">
            <input type="hidden" name="action" value="buyStock">
            <input type="hidden" name="userEmail" value="${user.email}">
            <input type="hidden" name="stockSymbol" value="${stockPrice.symbol}">
            <label>Shares to Buy:</label>
            <input type="number" name="sharesToBuy" min="0" 
                   max="1000000" step="1" value="0" />
            <label>&nbsp;</label>
            <input type="submit" value="Buy Shares">
        </form>
            <a href="<c:url value='/marketSim?action=displayProfile'/>">Cancel</a>
    </body>
</html>

<%-- 
    Document   : sellStock
    Created on : Nov 27, 2017, 9:21:27 PM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sell Stock</title>
    </head>
    <body>
        <p>You have ${sharesOfSymbolHeld} shares of ${symbol}.</p>
        <form action="<c:url value='/marketSim'/>" 
              method="post" class="inline">
            <input type="hidden" name="action" value="sellStock">
            <input type="hidden" name="symbolToSell" value="${symbol}">
            <label>How many shares would you like to sell?</label>
            <input type="number" name="numberOfSharesToSell" min="1" 
                   max="${sharesOfSymbolHeld}" step="1" value="" />
            <label>&nbsp;</label>
            <input type="submit" value="Sell Stock">
        </form>
    </body>
</html>

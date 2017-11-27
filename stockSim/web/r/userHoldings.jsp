<%-- 
    Document   : userHoldings
    Created on : Nov 26, 2017, 6:13:49 PM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Portfolio Holdings</title>
        <link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
    </head>
    <body>
        <table>
            <tr>
                <th>Symbol</th>
                <th>Quantity Held</th>
                <th>Current Price</th>
            </tr>
            <c:forEach var="holding" items="${userHoldings}" varStatus="status">
            <tr>
                <td>${holding.symbol}</td>
                <td>${holding.quantityHeld}</td>
                <td><fmt:formatNumber value="${holdingsLatestPrice[status.index].close}" type="currency"/></td>
                <td><a href="/marketSim?action=sellStock&symbol=${holding.symbol}">Sell</a></td>
            </tr>
            </c:forEach>
        </table>
    </body>
</html>

<%-- 
    Document   : viewUserHoldings
    Created on : Dec 3, 2017, 4:59:57 PM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Portfolio Holdings for ${user.name}</title>
        <link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
    </head>
    <body>
        <h3>Holdings for ${user.name}</h3>
        <table>
            <tr>
                <th>Symbol</th>
                <th>Quantity Held</th>
                <th>Average Price Per Share</th>
                <th>Current Price</th>
                <th>Percent Change</th>
                
            </tr>
            <c:forEach var="holding" items="${userHoldings}" varStatus="status">
            <tr>
                <td>${holding.symbolOwned}</td>
                <td>${holding.quantityHeld}</td>
                <td><fmt:formatNumber value="${holding.averagePricePerShare}" type="currency"/></td>
                <td><fmt:formatNumber value="${holdingsLatestPrice[status.index].close}" type="currency"/></td>
                <td><fmt:formatNumber type = "number" 
         maxFractionDigits = "2" value = "${precentGainList[status.index]}"/>%</td>
            </tr>
            </c:forEach>
        </table>
    </body>
</html>
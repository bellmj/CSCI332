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
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">

    </head>
    <body>
        <div class="container">
            <table class="table table-bordered-hover">
                <thead>
                    <tr>
                        <th scope="col">Symbol</th>
                        <th scope="col">Quantity Held</th>
                        <th scope="col">Average Price Per Share</th>
                        <th scope="col">Current Price</th>
                        <th scope="col">Percent Change</th>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="holding" items="${userHoldings}" varStatus="status">
                        <tr>
                            <td scope="row">${holding.symbolOwned}</td>
                            <td >${holding.quantityHeld}</td>
                            <td><fmt:formatNumber value="${holding.averagePricePerShare}" type="currency"/></td>
                            <td><fmt:formatNumber value="${holdingsLatestPrice[status.index].close}" type="currency"/></td>
                            <td><fmt:formatNumber type = "number" 
                                              maxFractionDigits = "2" value = "${precentGainList[status.index]}"/>%</td>
                            <td><a href="/marketSim?action=sellStock&symbol=${holding.symbolOwned}">Sell</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>

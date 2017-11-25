<%-- 
    Document   : apiData
    Created on : Nov 25, 2017, 3:54:20 PM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>API Data</title>
    </head>
    <body>
        <table>
            <tr>
                <th>Symbol</th>
                <th>timeStamp</th>
                <th>high</th>
                <th>low</th>
                <th>open</th>
                <th>close</th>
                <th>volume</th>
              
            </tr>
            <c:forEach var="stockPrice" items="${stockList}">
            <tr>
                <td>${stockPrice.symbol}</td>
                <td>${stockPrice.timeStamp}</td>
                <td>${stockPrice.high}</td>
                <td>${stockPrice.low}</td>
                <td>${stockPrice.open}</td>
                <td>${stockPrice.close}</td>
                <td>${stockPrice.volume}</td>
            </tr>
            </c:forEach>
        </table>
    </body>
</html>

<%-- 
    Document   : stockQuote
    Created on : Nov 25, 2017, 12:32:46 PM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Stock Quote</title>
    </head>
    <body>
        <form action="<c:url value='/marketSim'/>" 
              method="post" class="inline">
            <input type="hidden" name="action" value="getQuote">
            <label>Ticker Symbol</label>
            <input list="symbols" name="symbol">
            <datalist id="symbols">
                <c:forEach var="symbol" items="${symbols}">
                <option value="${symbol}">
            </c:forEach>
                
            </datalist> 
            <label>&nbsp;</label>
            <input type="submit" value="Get Quote">
        </form>
    </body>
</html>

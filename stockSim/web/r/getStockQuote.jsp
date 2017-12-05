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
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
        <link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
        
    </head>
    <body>
        <div class="container topMargin">
            <form action="<c:url value='/marketSim'/>" 
                  method="post" class="inline">
                <input type="hidden" name="action" value="getQuote">
                <input type="hidden" name="userEmail" value="${user.email}">
                <div class="form-group">
                    <label>Ticker Symbol</label>
                    <input list="symbols" name="symbol">
                    <datalist id="symbols">
                        <c:forEach var="symbol" items="${symbols}">
                            <option value="${symbol}">
                            </c:forEach>

                    </datalist> 
                

                <button type="submit" class="btn btn-primary">Get Quote</button>
                </div>
            </form>
        </div>
    </body>
</html>

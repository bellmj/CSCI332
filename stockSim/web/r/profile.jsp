<%-- 
    Document   : profile
    Created on : Nov 19, 2017, 2:46:19 AM
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
        <a href="<c:url value=''/>">Buy Stock</a><a href="<c:url value=''/>">View My Stocks</a>
        <p>Welcome ${user.name}</p>
        <br>
        <p>Your current account balance is $${user.accountBalance}</p>
    </body>
</html>

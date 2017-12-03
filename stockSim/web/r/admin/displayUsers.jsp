<%-- 
    Document   : displayUsers
    Created on : Nov 25, 2017, 3:42:58 AM
    Author     : Matthew Bell
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Info</title>
        <link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
    </head>
    <body>
        <a href="<c:url value='/r/admin/adminHome.jsp'/>">Admin Home</a>
        <table>
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Account Balance</th>
            </tr>
            <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>$${user.accountBalance}</td>
                <td><a href="<c:url value='/marketSim?action=adminViewUserHoldings&email=${user.email}'/>">View Holdings
                    </a></td>
            </tr>
            </c:forEach>
        </table>
    </body>
</html>

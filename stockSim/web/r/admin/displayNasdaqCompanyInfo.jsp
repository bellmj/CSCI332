<%-- 
    Document   : displayNasdaqCompanyInfo
    Created on : Nov 25, 2017, 1:56:41 AM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nasdaq Info</title>
        <link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
    </head>
    <body>
        <a href="<c:url value='/r/admin/adminHome.jsp'/>">Admin Home</a>
        <table>
            <tr>
                <th>Symbol</th>
                <th>Name</th>
                <th>Sector</th>
                <th>Industry</th>
            </tr>
            <c:forEach var="company" items="${companies}">
            <tr>
                <td>${company.symbol}</td>
                <td>${company.name}</td>
                <td>${company.sector}</td>
                <td>${company.industry}</td>
            </tr>
            </c:forEach>
        </table>
    </body>
</html>

<%-- 
    Document   : adminHome
    Created on : Nov 25, 2017, 2:15:29 AM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome Admin</title>
    </head>
    <body>
        <h1>Admin Home Page</h1>
        <a href="<c:url value='/marketSim?action=adminViewNyseInfoTable'/>">View Nyse Company List</a>
        <br>
        <a href="<c:url value='/marketSim?action=adminViewNasdaqInfoTable'/>">View Nasdaq Company List</a>
        <br>
        <a href="<c:url value='/marketSim?action=adminViewUserTable'/>">View User</a>
        
        
    </body>
</html>

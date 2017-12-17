<%-- 
    Document   : logout
    Created on : Dec 9, 2017, 9:46:23 AM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>
<% session.invalidate(); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="refresh" content="1; url=/index.html" />
        <title>logout</title>
    </head>
    <body>
        <%=request.getRemoteUser()%> has been logged out
    </body>
</html>

<%-- 
    Document   : changePass
    Created on : Dec 3, 2017, 4:30:03 PM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change User Pass</title>
    </head>
    <body>
        <form action="<c:url value='/marketSim'/>" 
              method="post" class="inline">
            <input type="hidden" name="action" value="changePass">
            <label>Password</label>
            <input type="password" name="password" 
                   value="" required><span style="color:#f00">${oldPasswordErrorMessage}</span>
            <br>
            <label>Password Confirm</label>
            <input type="password" name="newPassword" 
                   value="" required><span style="color:#f00">${newPasswordErrorMessage}</span>
            <br>
            <label>Password Confirm</label>
            <input type="password" name="newPasswordConfirm" 
                   value="" required>
            <label>&nbsp;</label>
            <input type="submit" value="Change password">
        </form>
    </body>
</html>

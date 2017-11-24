<%-- 
    Document   : signup
    Created on : Nov 17, 2017, 11:30:11 AM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
        <title>Sign Up</title>
    </head>
    <body>
        <form action="<c:url value='/marketSim'/>" 
              method="post" class="inline">
            <input type="hidden" name="action" value="signup">
            <label>Full Name</label>
            <input type="text" name="name"
                   value="${user.name}" required><span style="color:#f00">${nameErrorMessage}</span>
            <br>
            <label>Email</label>
            <input type="email" name="email"
                   value="${user.email}" required><span style="color:#f00">${emailErrorMessage}</span>
            <br>
            <label>Password</label>
            <input type="password" name="password" 
                   value="" required><span style="color:#f00">${passwordErrorMessage}</span>
            <br>
            <label>Password Confirm</label>
            <input type="password" name="passwordConfirm" 
                   value="" required>
            <br>
            <label>Initial Account Balance(max:$1,000,000)</label>
            <input type="number" name="initialBalance" min="0.00" 
                   max="1000000.00" step="0.01" value="${user.accountBalance}" />
            <label>&nbsp;</label>
            <input type="submit" value="Sign Up">
        </form>
    </body>
</html>

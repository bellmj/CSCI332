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
            
            <label>Email</label>
            <input type="text" name="email"
                   value="">
           <br>

            <label>Password</label>
            <input type="password" name="password" 
                   value="">
            <br>
             <label>Password Confirm</label>
            <input type="password" name="passwordConfirm" 
                   value="">
            <br>

            <label>&nbsp;</label>
            <input type="submit" value="Sign Up">
        </form>
    </body>
</html>

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
        <a href="<c:url value='/marketSim?action=stockQuoteHome'/>">Get Stock Quote</a>
        <br>
        <a href="<c:url value='/marketSim?action=viewUserHoldings'/>">View My Stocks</a>
        <p>Welcome ${user.name}<a href="<c:url value='/marketSim?action=editUserName'/>"> (Edit Name)</a></p>
        <p>Your current account balance is $${user.accountBalance}
            <a href="<c:url value='/marketSim?action=editAccountBalance'/>">(Edit)</a></p>
        <a href="<c:url value='/marketSim?action=changePassword'/>">Change Password</a>
        <br>
        <a href="<c:url value='${adminHomeLink}'/>">${adminHomeText}</a>
    </body>
</html>

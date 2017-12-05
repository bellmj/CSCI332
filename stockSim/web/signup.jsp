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
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
        <title>Sign Up</title>
    </head>
    <body>
        <div class="container topMargin">
            <div class="row">
                <div class="col">

                </div>
                <div class="col">
                    <form action="<c:url value='/marketSim'/>" 
                          method="post" class="inline">
                        <div class="form-group">
                            <input type="hidden"  name="action" value="signup">
                            <label>Full Name</label>
                            <input type="text" name="name"
                                   value="${user.name}" class="form-control" required><span style="color:#f00">${nameErrorMessage}</span>
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" name="email"
                                   value="${user.email}" class="form-control" required><span style="color:#f00">${emailErrorMessage}</span>
                        </div>
                        <div class="form-group">
                            <label>Password</label>
                            <input type="password" name="password" 
                                   value="" class="form-control" required><span style="color:#f00">${passwordErrorMessage}</span>
                        </div>
                        <div class="form-group">
                            <label>Password Confirm</label>
                            <input type="password" name="passwordConfirm" 
                                   value="" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Initial Account Balance (max:$1,000,000)</label>
                            <input type="number" name="initialBalance" min="0.00" 
                                   max="1000000.00" step="0.01" class="form-control" value="${user.accountBalance}" />
                        </div>
                        <button type="submit" class="btn btn-primary">Sign Up</button>
                    </form>
                </div>
                <div class="col"></div>
            </div>
    </body>
</html>

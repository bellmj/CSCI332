<%-- 
    Document   : stockQuote
    Created on : Nov 25, 2017, 1:43:04 PM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
        <link rel="stylesheet" href="/styles/css/bootstrap.min.css">
        <link rel="stylesheet" href="/styles/css/bootstrap-theme.min.css">
        <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
        <script src="styles/js/bootstrap.min.js"></script>

        <title>Stock Quote</title>
    </head>
    <body>
        <nav class="navbar navbar-toggleable-sm navbar-light bg-faded">
            <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <a class="navbar-brand" href="#">StockSimulator2000</a>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="<c:url value='/marketSim?action=stockQuoteHome'/>">Get Stock Quote</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="<c:url value='/marketSim?action=viewUserHoldings'/>">View My Stocks</a>
                    </li>

                </ul>
            </div>
        </nav>
        <div class="container marginTop">
            <p>Latest Stock Price For ${stockPrice.symbol} is <fmt:formatNumber value="${stockPrice.close}" type="currency"/></p>
            <p>You currently have <fmt:formatNumber value="${user.accountBalance}" type="currency"/> in your account</p>
            <p>Would you like to buy some shares?</p>
            <form action="<c:url value='/marketSim'/>" 
                  method="post" class="inline">
                <div class="form-group">
                    <input type="hidden" name="action" value="buyStock">
                    <input type="hidden" name="userEmail" value="${user.email}">
                    <input type="hidden" name="stockSymbol" value="${stockPrice.symbol}">
                    <label>Shares to Buy:</label>
                    <input type="number" name="sharesToBuy" min="0" 
                           max="${maxStock}" step="1" value="0" />
                </div>
                <div>
                    <button type="submit" class="btn btn-primary">Buy Shares</button>
                </div>
            </form>
                <div class="buttonContianer">
            <a href="<c:url value='/marketSim?action=displayProfile'/>" class="btn btn-primary">Cancel</a>
                </div>
        </div>
    </body>
</html>

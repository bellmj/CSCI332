<%-- 
    Document   : sellStock
    Created on : Nov 27, 2017, 9:21:27 PM
    Author     : Matthew Bell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
        <link rel="stylesheet" href="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css"/>" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
        <link rel="stylesheet" href="/styles/css/bootstrap.min.css">
        <link rel="stylesheet" href="/styles/css/bootstrap-theme.min.css">
        <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
        <script src="styles/js/bootstrap.min.js"></script>
        <title>Sell Stock</title>
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
            <p>You have ${sharesOfSymbolHeld} shares of ${symbol}.</p>
            <form action="<c:url value='/marketSim'/>" 
                  method="post" class="inline">
                 <div class="form-group">
                <input type="hidden" name="action" value="sellStock">
                <input type="hidden" name="symbolToSell" value="${symbol}">
                <label>How many shares would you like to sell?</label>
                <input type="number" name="numberOfSharesToSell" min="1" 
                       max="${sharesOfSymbolHeld}" step="1" value="" />
                <label>&nbsp;</label>
                <input type="submit" class="btn btn-primary" value="Sell Stock">
            </form>
        </div>
    </body>
</html>

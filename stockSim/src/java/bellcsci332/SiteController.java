/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bellcsci332;

import bellcsci332.business.CompanyInfo;
import bellcsci332.business.PortfolioHolding;
import bellcsci332.business.SimplePortfolioHolding;
import bellcsci332.business.StockPrice;
import bellcsci332.business.User;
import bellcsci332.data.DBUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Matthew Bell
 */
public class SiteController extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse 
            response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        Logger.getLogger(SiteController.class.getName()).log(Level.INFO, 
                action);
        String url = "null";
        if (action == null) {
            url = "/index.html";
        } else if (action.equals("signup")) {
            url = "/signup.jsp";
        } else if (action.equals("login")) {
            url = "/r/welcomeuser.jsp";
        } else if (action.equals("displayProfile")) {
            String userEmail = request.getUserPrincipal().getName();
            boolean userIsAdmin = request.isUserInRole("admin");
            if (!userIsAdmin) {
                request.setAttribute("user", DBUtil.getUser(userEmail));
                url = "/r/profile.jsp";
            } else {
                request.setAttribute("adminHomeLink", "/r/admin/adminHome.jsp");
                request.setAttribute("adminHomeText", "Admin Home");
                request.setAttribute("user", DBUtil.getUser(userEmail));
                url = "/r/profile.jsp";
            }
        } else if (action.equals("adminViewNyseInfoTable")) {
            //todo get NyseInfoTable and pass it to displayNyseCompanyInfo.jsp
            boolean userIsAdmin = request.isUserInRole("admin");
            if (userIsAdmin) {
                List<CompanyInfo> nyseCompanyInfo = DBUtil.getNyseCompaniesInfo();
                request.setAttribute("companies", nyseCompanyInfo);
                url = "/r/admin/displayNyseCompanyInfo.jsp";
            } else {
                url ="/index.html";
            }

        } else if (action.equals("adminViewNasdaqInfoTable")) {
            boolean userIsAdmin = request.isUserInRole("admin");
            if (userIsAdmin) {
                List<CompanyInfo> nyseCompanyInfo = DBUtil.getNasdaqCompaniesInfo();
                request.setAttribute("companies", nyseCompanyInfo);
                url = "/r/admin/displayNasdaqCompanyInfo.jsp";
            } else {
                url = "/index.html";
            }

        } else if (action.equals("adminViewUserTable")) {
            boolean userIsAdmin = request.isUserInRole("admin");
            if (userIsAdmin) {
                List<User> users = DBUtil.getUsers();
                request.setAttribute("users", users);
                url = "/r/admin/displayUsers.jsp";
            } else {
                url = "/index.html";
            }

        } else if (action.equals("stockQuoteHome")) {
            request.setAttribute("symbols", DBUtil.getAllTickerSymbols());
            //^^pass a list of all symbols to the stockQuote jsp
            request.setAttribute("userEmail", "userEmial");
            url = "/r/getStockQuote.jsp";
        } else if (action.equals("viewUserHoldings")) {
            List<SimplePortfolioHolding> userHoldings
                    = DBUtil.getPortfolio(request.getUserPrincipal().getName())
                            .getPortfolioHoldings();
            List<StockPrice> latestStockPrice = new ArrayList<>();
            List<BigDecimal> precentGainList = new ArrayList<>();
            List<BigDecimal> totalReturnList = new ArrayList<>();
            List<BigDecimal> totalEquityValue = new ArrayList<>();
            BigDecimal portfolioReturn = new BigDecimal(0);
            BigDecimal portfolioEquity = new BigDecimal(0);
            for (SimplePortfolioHolding ph : userHoldings) {
                latestStockPrice.add(DBUtil.getLatestStockPrice(
                        ph.getSymbolOwned()));
                        //TODO maybe optimize these method calls
                BigDecimal precentChange = ph.getAveragePricePerShare()
                        .subtract(latestStockPrice.get(latestStockPrice.size()
                                - 1).getClose());
//                Logger.getLogger(SiteController.class.getName()).log(
//                  Level.INFO,precentChange.toString());
                precentChange = precentChange.divide(
                        ph.getAveragePricePerShare(), 10, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("-100.0"));
//                Logger.getLogger(SiteController.class.getName())
//                  .log(Level.INFO,precentChange.toString());
                precentGainList.add(precentChange);
                BigDecimal totalCost = ph.getAveragePricePerShare().multiply(
                        new BigDecimal(ph.getQuantityHeld()));
                BigDecimal equity = latestStockPrice.get(latestStockPrice.size()
                        - 1).getClose().multiply(new BigDecimal(
                                ph.getQuantityHeld()));
                BigDecimal totalReturn = equity.subtract(totalCost);
                totalReturnList.add(totalReturn);
                portfolioReturn = portfolioReturn.add(totalReturn);
                totalEquityValue.add(equity);
                portfolioEquity = portfolioEquity.add(equity);
      
            }
            request.setAttribute("portfolioReturn",portfolioReturn);
            request.setAttribute("portfolioEquity", portfolioEquity);
            request.setAttribute("totalReturnList", totalReturnList);
            request.setAttribute("equityList", totalEquityValue);
            request.setAttribute("precentGainList", precentGainList);
            request.setAttribute("holdingsLatestPrice", latestStockPrice);
            request.setAttribute("userHoldings", userHoldings);
            url = "/r/userHoldings.jsp";
        } else if (action.equals("sellStock")) {
            String symbol = request.getParameter("symbol");
            int sharesOfSymbolHeld = DBUtil.getNumberOfHoldingsForStock(symbol,
                    request.getUserPrincipal().getName());
            request.setAttribute("symbol", symbol);
            request.setAttribute("sharesOfSymbolHeld", sharesOfSymbolHeld);
            url = "/r/sellStock.jsp";
        } else if (action.equals("editUserName")) {
            String userEmail = request.getUserPrincipal().getName();
            request.setAttribute("user", DBUtil.getUser(userEmail));
            url = "/r/editUserName.jsp";
        } else if (action.equals("editAccountBalance")) {
            String userEmail = request.getUserPrincipal().getName();
            request.setAttribute("user", DBUtil.getUser(userEmail));
            url = "/r/editAccountBalance.jsp";
        } else if (action.equals("changePassword")) {
            url = "/r/changePass.jsp";
        } else if (action.equals("adminViewUserHoldings")) {
            boolean userIsAdmin = request.isUserInRole("admin");
            if (userIsAdmin) {
                List<SimplePortfolioHolding> userHoldings
                        = DBUtil.getPortfolio((String) request.getParameter("email"
                        )).getPortfolioHoldings();
                List<StockPrice> latestStockPrice = new ArrayList<>();
                List<BigDecimal> precentGainList = new ArrayList<>();
                for (SimplePortfolioHolding ph : userHoldings) {
                    latestStockPrice.add(DBUtil.getLatestStockPrice(
                            ph.getSymbolOwned()));
                            //TODO maybe optimize these method calls
                    BigDecimal precentChange = ph.getAveragePricePerShare()
                            .subtract(latestStockPrice.get(latestStockPrice.size()
                                    - 1).getClose());
    //                Logger.getLogger(SiteController.class.getName()).
                        //log(Level.INFO,precentChange.toString());
                    precentChange = precentChange.divide(
                            ph.getAveragePricePerShare(), 10, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("-100.0"));
    //                Logger.getLogger(SiteController.class.getName()).
                        //log(Level.INFO,precentChange.toString());
                    precentGainList.add(precentChange);
                }
                request.setAttribute("precentGainList", precentGainList);
                request.setAttribute("holdingsLatestPrice", latestStockPrice);
                request.setAttribute("userHoldings", userHoldings);
                request.setAttribute("user", DBUtil.getUser((String) 
                        request.getParameter("email")));
                url = "/r/admin/viewUserHoldings.jsp";
            }
            else {
                url = "/index.html";
            }
        } else if (action.equals("adminEnterUserMode")) {
            String userEmail = request.getUserPrincipal().getName();
            boolean userIsAdmin = request.isUserInRole("admin");
            if (!userIsAdmin) {
                request.setAttribute("user", DBUtil.getUser(userEmail));
                url = "/r/profile.jsp";
            } else {
                request.setAttribute("adminHomeLink", "/r/admin/adminHome.jsp");
                request.setAttribute("adminHomeText", "Admin Home");
                request.setAttribute("user", DBUtil.getUser(userEmail));
                url = "/r/profile.jsp";
            }
        } else {
            url = "/index.html";
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String url = "/index.html";
        if (action == null) {
            url = "/signup.jsp";
        } else if (action.equals("signup")) {
            url = signUp(request, response);
        } else if (action.equals("getQuote")) {
            url = getQuote(request, response);
        } else if (action.equals("buyStock")) {
            url = buyStock(request, response);
        } else if (action.equals("sellStock")) {
            url = sellStock(request, response);
        } else if (action.equals("editUserName")) {
            String newName = request.getParameter("name");
            User user = DBUtil.getUser(request.getUserPrincipal().getName());
            user.setName(newName);
            DBUtil.updateUser(user);
            boolean userIsAdmin = request.isUserInRole("admin");
            if (!userIsAdmin) {
                request.setAttribute("user", user);
                url = "/r/profile.jsp";
            } else {
                request.setAttribute("adminHomeLink", "/r/admin/adminHome.jsp");
                request.setAttribute("adminHomeText", "Admin Home");
                request.setAttribute("user", user);
                url = "/r/profile.jsp";
            }
        } else if (action.equals("editUserAccountBalance")) {
            BigDecimal newAccountBalance = new BigDecimal(
                    request.getParameter("accountBalance"));
            User user = DBUtil.getUser(request.getUserPrincipal().getName());
            user.setAccountBalance(newAccountBalance);
            DBUtil.updateUser(user);
            boolean userIsAdmin = request.isUserInRole("admin");
            if (!userIsAdmin) {
                request.setAttribute("user", user);
                url = "/r/profile.jsp";
            } else {
                request.setAttribute("adminHomeLink", "/r/admin/adminHome.jsp");
                request.setAttribute("adminHomeText", "Admin Home");
                request.setAttribute("user", user);
                url = "/r/profile.jsp";
            }
        } else if (action.equals("changePass")) {
            User user = DBUtil.getUser(request.getUserPrincipal().getName());
            String oldPass = request.getParameter("password");
            String newPassword = request.getParameter("newPassword");
            String newPasswordConfirm = request.getParameter(
                    "newPasswordConfirm");
            if (!newPassword.equals(newPasswordConfirm)) {
                request.setAttribute("newPasswordErrorMessage", 
                        "New passwords do not match");
                url = "/r/changePass.jsp";
            }
            if (!oldPass.equals(user.getPassword())) {
                request.setAttribute("oldPasswordErrorMessage", 
                        "User password Invalid");
                url = "/r/changePass.jsp";
            }
            if (newPassword.equals(newPasswordConfirm) && oldPass.equals(
                    user.getPassword())) {
                user.setPassword(newPassword);
                DBUtil.updateUser(user);
                boolean userIsAdmin = request.isUserInRole("admin");
                if (!userIsAdmin) {
                    request.setAttribute("user", user);
                    url = "/r/profile.jsp";
                } else {
                    request.setAttribute("adminHomeLink", 
                            "/r/admin/adminHome.jsp");
                    request.setAttribute("adminHomeText", "Admin Home");
                    request.setAttribute("user", user);
                    url = "/r/profile.jsp";
                }
            }
        }
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    private String sellStock(HttpServletRequest request, 
            HttpServletResponse response) {
        String url = "/index.html";
        String symbolToSell = request.getParameter("symbolToSell");
        int quantityToSell = Integer.parseInt(request.getParameter(
                "numberOfSharesToSell"));
        String userEmail = request.getUserPrincipal().getName();
        DBUtil.sellStock(userEmail, symbolToSell, quantityToSell);
        boolean userIsAdmin = request.isUserInRole("admin");
        if (!userIsAdmin) {
            request.setAttribute("user", DBUtil.getUser(userEmail));
            url = "/r/profile.jsp";
        } else {
            request.setAttribute("adminHomeLink", "/r/admin/adminHome.jsp");
            request.setAttribute("adminHomeText", "Admin Home");
            request.setAttribute("user", DBUtil.getUser(userEmail));
            url = "/r/profile.jsp";
        }
        return url;
    }

    private String buyStock(HttpServletRequest request, 
            HttpServletResponse response) {
        String url = "/index.html";
        String selectedSymbol = request.getParameter("stockSymbol");
        String userEmail = request.getUserPrincipal().getName();
        int numberOfShares = Integer.parseInt(
                request.getParameter("sharesToBuy"));
        DBUtil.buyStock(userEmail, numberOfShares, selectedSymbol);
        boolean userIsAdmin = request.isUserInRole("admin");
        if (!userIsAdmin) {
            request.setAttribute("user", DBUtil.getUser(userEmail));
            url = "/r/profile.jsp";
        } else {
            request.setAttribute("adminHomeLink", "/r/admin/adminHome.jsp");
            request.setAttribute("adminHomeText", "Admin Home");
            request.setAttribute("user", DBUtil.getUser(userEmail));
            url = "/r/profile.jsp";
        }
        return url;
    }

    private String getQuote(HttpServletRequest request, 
            HttpServletResponse response) {
        String selectedSymbol = request.getParameter("symbol");
        String url = "/index.html";
        boolean symbolInNasdaq = DBUtil.symbolInNasdaq(selectedSymbol);
        boolean symbolInNyse = DBUtil.symbolInNyse(selectedSymbol);
        if (!(symbolInNasdaq || symbolInNyse)) {
            //TODO redirect back to getStockQuote.jsp with an error message
            url = "/getStockQuote.jsp";
        } else {

            //TODO check Nasdaq or NYSEPricesByTheMinute to see if symbol 
            // is in table and up to date
            StockPrice latestStockPrice = DBUtil.getLatestStockPrice(
                    selectedSymbol);
            request.setAttribute("stockPrice", latestStockPrice);
            String userEmail = request.getUserPrincipal().getName();
            User user = DBUtil.getUser(userEmail);
            int maxStock = user.getAccountBalance().divide(
                    latestStockPrice.getClose(), 10, RoundingMode.HALF_UP)
                    .intValue();
//            Logger.getLogger(SiteController.class.getName())
                //.log(Level.INFO,"max Stock is:" + maxStock);
            request.setAttribute("maxStock",maxStock);
            request.setAttribute("user", user);
            url = "/r/stockQuote.jsp";
        }

        return url;
    }

    private String signUp(HttpServletRequest request, HttpServletResponse 
            response) {
        User newUser;
        String url = "/index.html";
        
        MessageDigest digest;
        String hashedPass = "";
        String hashedPassConfirm = "";
       
        try {// try to hash the provided passwords
            //TODO generate random salts here and load them into the DB
            digest = MessageDigest.getInstance("SHA-512");
            byte[] hashedPassByteArray = digest.digest(
                    request.getParameter("password").getBytes("UTF-8"));
            byte[] hashedPassConfirmByteArray = digest.digest(
                    request.getParameter("passwordConfirm").getBytes("UTF-8"));
            hashedPass = DatatypeConverter.printHexBinary(hashedPassByteArray);
            hashedPassConfirm = DatatypeConverter.printHexBinary(
                    hashedPassConfirmByteArray);
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SiteController.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SiteController.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
	Logger.getLogger(SiteController.class.getName()).log(
                Level.INFO,"Hashed Pass:" + hashedPass);

        String userName = (String) request.getParameter("name");
        String password = (String) request.getParameter("password");
        String passConfirm = (String) request.getParameter("passwordConfirm");
        String userEmail = (String) request.getParameter("email");
        BigDecimal initialAccountBalance = new BigDecimal((String) 
                request.getParameter("initialBalance"));

        
        newUser = new User(userEmail,userName,initialAccountBalance,password);

        if (userName.matches(DBUtil.NAME_REGEX) && userEmail.matches(
                DBUtil.EMAIL_REGEX)
                && password.equals(passConfirm) && !password.equals("")) {
            DBUtil.addUser(newUser);
            url = "/welcomesignup.jsp";

        } else {
            //TODO ensure users email is not already contained in the DB
            if (!userName.matches(DBUtil.NAME_REGEX)) {
                request.setAttribute("nameErrorMessage",
                        "(Name must include first and last name only seperated "
                                + "by a space)");
            }
            if (!userEmail.matches(DBUtil.EMAIL_REGEX)) {
                request.setAttribute("emailErrorMessage", 
                        "(Please enter a valid email)");
            }
            if (!hashedPass.equals(hashedPassConfirm) || 
                    hashedPass.equals("")) {
                request.setAttribute("passwordErrorMessage", 
                        "(Passwords don't match)");
            }
            request.setAttribute("user", newUser);
            url = "/signup.jsp";
        }

        return url;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

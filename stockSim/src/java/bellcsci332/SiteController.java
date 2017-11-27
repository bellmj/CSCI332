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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author Matthew Bell
 */
public class SiteController extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
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
                url = "/r/admin/adminHome.jsp";
            }
        } else if (action.equals("adminViewNyseInfoTable")) {
            //todo get NyseInfoTable and pass it to displayNyseCompanyInfo.jsp
            List<CompanyInfo> nyseCompanyInfo = DBUtil.getNyseCompaniesInfo();
            request.setAttribute("companies", nyseCompanyInfo);
            url = "/r/admin/displayNyseCompanyInfo.jsp";

        } else if (action.equals("adminViewNasdaqInfoTable")) {
            List<CompanyInfo> nyseCompanyInfo = DBUtil.getNasdaqCompaniesInfo();
            request.setAttribute("companies", nyseCompanyInfo);
            url = "/r/admin/displayNasdaqCompanyInfo.jsp";

        } else if (action.equals("adminViewUserTable")) {
            List<User> users = DBUtil.getUsers();
            request.setAttribute("users", users);
            url = "/r/admin/displayUsers.jsp";

        } else if (action.equals("stockQuoteHome")) {
            request.setAttribute("symbols", DBUtil.getAllTickerSymbols());//pass a list of all symbols to the stockQuote jsp
            request.setAttribute("userEmail", "userEmial");
            url = "/r/getStockQuote.jsp";
        } else if(action.equals("viewUserHoldings")){
            List<SimplePortfolioHolding> userHoldings = 
                    DBUtil.getPortfolio(request.getUserPrincipal().getName()).getPortfolioHoldings();
            List<StockPrice> latestStockPrice = new ArrayList<>();
            List<BigDecimal> precentGainList = new ArrayList<>();
            for(SimplePortfolioHolding ph:userHoldings){
                latestStockPrice.add(DBUtil.getLatestStockPrice(ph.getSymbolOwned()));//TODO maybe optimize these method calls
                BigDecimal precentChange = ph.getAveragePricePerShare().subtract(latestStockPrice.get(latestStockPrice.size()-1).getClose())
                        .divide(ph.getAveragePricePerShare()).multiply(new BigDecimal("100.0"));
                Logger.getLogger(SiteController.class.getName()).log(Level.INFO,precentChange.toString());
                precentGainList.add(precentChange);
            }
            request.setAttribute("precentGainList",precentGainList);
            request.setAttribute("holdingsLatestPrice",latestStockPrice);
            request.setAttribute("userHoldings", userHoldings);
            url = "/r/userHoldings.jsp";
        }else {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String url = "/index.html";
        if (action == null) {
            url = "/signup.jsp";
        } else if (action.equals("signup")) {
            url = signUp(request, response);
        } else if (action.equals("getQuote")) {
            url = getQuote(request, response);
        } else if (action.equals("buyStock")){
            url = buyStock(request,response);
        }
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
    private String buyStock(HttpServletRequest request,HttpServletResponse response){
        String url = "/index.html";
        String selectedSymbol = request.getParameter("stockSymbol");
        String userEmail = request.getUserPrincipal().getName();
        int numberOfShares = Integer.parseInt(request.getParameter("sharesToBuy"));
        DBUtil.buyStock(userEmail, numberOfShares, selectedSymbol);
        request.setAttribute("user", DBUtil.getUser(userEmail));
        url = "/r/profile.jsp";
        return url;
    }
    private String getQuote(HttpServletRequest request, HttpServletResponse response) {
        String selectedSymbol = request.getParameter("symbol");
        String url = "/index.html";
        boolean symbolInNasdaq = DBUtil.symbolInNasdaq(selectedSymbol);
        boolean symbolInNyse = DBUtil.symbolInNyse(selectedSymbol);
        if (!(symbolInNasdaq || symbolInNyse)) {
            //TODO redirect back to getStockQuote.jsp with an error message
            url = "/getStockQuote.jsp";
        } else {

            //TODO check Nasdaq or NYSEPricesByTheMinute to see if symbol is in table and up to date
            StockPrice latestStockPrice = DBUtil.getLatestStockPrice(selectedSymbol);
            request.setAttribute("stockPrice", latestStockPrice);
            String userEmail = request.getUserPrincipal().getName();
            User user = DBUtil.getUser(userEmail);
            request.setAttribute("user", user);
            url = "/r/stockQuote.jsp";
        }

        return url;
    }

    private String signUp(HttpServletRequest request, HttpServletResponse response) {
        User newUser = new User();
        String url = "/index.html";
        //TODO validate user input; redirect back to signup if something is wrong
        //TODO hashUser password
        String userName = (String) request.getParameter("name");
        String userEmail = (String) request.getParameter("email");
        String userPassword1 = (String) request.getParameter("password");
        String userPassword2 = (String) request.getParameter("passwordConfirm");
        BigDecimal initialAccountBalance = new BigDecimal((String) request.getParameter("initialBalance"));

        newUser.setName(userName);
        newUser.setEmail((String) request.getParameter("email"));
        newUser.setAccountBalance(initialAccountBalance);
        newUser.setPassword((String) request.getParameter("password"));

        if (userName.matches(DBUtil.NAME_REGEX) && userEmail.matches(DBUtil.EMAIL_REGEX)
                && userPassword1.equals(userPassword2)) {

            DBUtil.addUser(newUser);
            request.setAttribute("user", newUser);
            url = "/r/profile.jsp";
        } else {
            //TODO ensure that the users email is not already contained in the DB
            if (!userName.matches(DBUtil.NAME_REGEX)) {
                request.setAttribute("nameErrorMessage", "(Name must include first and last name only seperated by a space)");
            }
            if (!userEmail.matches(DBUtil.EMAIL_REGEX)) {
                request.setAttribute("emailErrorMessage", "(Please enter a valid email)");
            }
            if (!userPassword1.equals(userPassword2)) {
                request.setAttribute("passwordErrorMessage", "(Passwords don't match)");
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

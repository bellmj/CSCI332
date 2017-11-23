/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bellcsci332;

import bellcsci332.business.User;
import bellcsci332.data.DBUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
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
            request.setAttribute("user", DBUtil.getUser(userEmail));
            url = "/r/profile.jsp";
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String url = "";
        if (action == null) {
            url = "/signup.jsp";
        } else if (action.equals("signup")) {
            url = signUp(request, response);
        }
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
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
            if(!userPassword1.equals(userPassword2)){
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

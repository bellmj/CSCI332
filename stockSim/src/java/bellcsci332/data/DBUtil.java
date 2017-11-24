/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bellcsci332.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import bellcsci332.business.*;

/**
 *
 * @author Matthew Bell
 */
public class DBUtil {

    public final static String NAME_REGEX = "([a-z]|[A-Z]|'|-)+ ([a-z]|[A-Z]|'|-)+";
    public static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";//Taken from http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/

    /**
     * a method to obtain a user from the database using their email
     *
     * @param email the email of the user to obtain
     * @return A User object that represents the user in the database; null if
     * this user does not exist
     */
    public static User getUser(String email) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Users "
                + "WHERE email = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            User user = null;
            if (rs.next()) {
                user = new User();
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setAccountBalance(rs.getBigDecimal("accountBalance"));
            }
            return user;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    public static void addUser(User newUser) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "INSERT INTO Users(email,name,accountBalance,password) "
                + "VALUES(?,?,?,?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, newUser.getEmail());
            ps.setString(2, newUser.getName());
            ps.setBigDecimal(3, newUser.getAccountBalance());
            ps.setString(4, newUser.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }
    public static UserRole getUsersRole(){
        //TODO implement this method
        return null;
    } 
}

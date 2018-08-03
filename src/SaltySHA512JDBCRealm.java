/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.JDBCRealm;

/**
 * This class is intended to extend the JDBCRealm class provided by Tomcat,
 * additionally providing password hashing using BCrypt and the use of random
 * salts for each user. Inspiration for this class was taken from:
 * "https://github.com/eneuwirt/fbs/blob/master/fbs-security/src/main/java/" +
 * "com/fbs/security/shiro/realm/SaltAwareJdbcRealm.java"
 * "https://stackoverflow.com/questions/12285604/writing-a-custom-tomcat-" +
 * "realm-using-bcrypt"
 *
 * @author Matthew Bell
 * @since 8 May 2018
 *
 */
public class SaltySHA512JDBCRealm extends JDBCRealm {

    @Override
    public Principal authenticate(String username, String credentials) {
        String usersSalt = getPasswordSaltForUser(username);
        credentials = usersSalt + credentials + usersSalt;
        String hashedPassword = getPassword(username);
        String providedHashedPass = null;
        if (hashedPassword == null || usersSalt == null) {
            return null;
        }
        if (username == null || credentials == null) {
            return null;
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-512");
            byte[] hashedPassByteArray = digest.digest(credentials.getBytes("UTF-8"));
            providedHashedPass = DatatypeConverter.printHexBinary(hashedPassByteArray);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SaltySHA512JDBCRealm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SaltySHA512JDBCRealm.class.getName()).log(Level.SEVERE, null, ex);
        }



        // Validate the user's credentials
        boolean validated = false;
        if(providedHashedPass != null && providedHashedPass.equalsIgnoreCase(hashedPassword)){
            validated = true;
        }

        ArrayList<String> roles = getRoles(username);
        return (validated?(new GenericPrincipal(username,providedHashedPass,roles)):null);
    }
    /**
     * returns DB salt for a given users email; null if user cannot be found
     * @param email
     * @return
     */
    private String getPasswordSaltForUser(String email) {
        Connection connection = dbConnection;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Users "
                + "WHERE email = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            String userSalt = null;
            if (rs.next()) {

                userSalt = rs.getString("salt");

            }
            return userSalt;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {

            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {

            }
        }
    }
}

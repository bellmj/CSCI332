/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bellcsci332.data;

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
        String providedHashedPass;
        if (hashedPassword == null) {
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
            
            
            
        // Look up the user's credentials
        String dbCredentials = getPassword(username);

        // Validate the user's credentials
        boolean validated = false;
        if (hasMessageDigest()) {
            // Hex hashes should be compared case-insensitive
            validated = (credentials.equalsIgnoreCase(dbCredentials));
        } else {
            validated = (credentials.equals(dbCredentials));
        }

        ArrayList<String> roles = getRoles(username);
        
        // Create and return a suitable Principal for this user
        return null;//(new GenericPrincipal(this, username, credentials, roles));
    }

    private String getPasswordSaltForUser(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        //TODO query DB for the salt associated with the String username
        return null;
    }

}

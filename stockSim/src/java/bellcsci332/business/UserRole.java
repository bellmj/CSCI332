/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bellcsci332.business;

import java.io.Serializable;

/**
 *
 * @author Matthew Bell
 */
public class UserRole implements Serializable {

    private String userEmail;
    private String role;

    public UserRole() {

    }

    public UserRole(String userEmail, String role) {
        this.userEmail = userEmail;
        this.role = role;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}

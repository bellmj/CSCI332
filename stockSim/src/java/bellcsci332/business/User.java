/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bellcsci332.business;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Matthew Bell
 */
public class User implements Serializable{
    private String email;
    private String name;
    private BigDecimal accountBalance;
    private String password;
    public User(){
        
    }
    public User(String email, String name, BigDecimal accountBalance, String password) {
        this.email = email;
        this.name = name;
        this.accountBalance = accountBalance;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   
    
}

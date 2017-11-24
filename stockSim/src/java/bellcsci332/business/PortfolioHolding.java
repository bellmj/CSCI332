/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bellcsci332.business;

import java.sql.Date;

/**
 *
 * @author Matthew Bell
 */
public class PortfolioHolding {
    private Date timeStamp;
    private int quantityHeld;
    private String ownerEmail;
    private String symbol;

    public PortfolioHolding() {
    }

    public PortfolioHolding(Date timeStamp, int quantityHeld, String ownerEmail, String symbol) {
        this.timeStamp = timeStamp;
        this.quantityHeld = quantityHeld;
        this.ownerEmail = ownerEmail;
        this.symbol = symbol;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getQuantityHeld() {
        return quantityHeld;
    }

    public void setQuantityHeld(int quantityHeld) {
        this.quantityHeld = quantityHeld;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    
}

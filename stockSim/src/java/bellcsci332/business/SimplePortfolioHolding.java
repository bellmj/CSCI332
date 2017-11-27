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
public class SimplePortfolioHolding implements Serializable {
    private String symbolOwned;
    private Integer quantityHeld;
    private BigDecimal averagePricePerShare;

    public SimplePortfolioHolding() {
    }

    public SimplePortfolioHolding(String symbolsOwned, Integer quantitiesHeld, BigDecimal averagePricesPerShare) {
        this.symbolOwned = symbolsOwned;
        this.quantityHeld = quantitiesHeld;
        this.averagePricePerShare = averagePricesPerShare;
    }

    public String getSymbolOwned() {
        return symbolOwned;
    }

    public void setSymbolOwned(String symbolsOwned) {
        this.symbolOwned = symbolsOwned;
    }

    public Integer getQuantityHeld() {
        return quantityHeld;
    }

    public void setQuantityHeld(Integer quantitiesHeld) {
        this.quantityHeld = quantitiesHeld;
    }

    public BigDecimal getAveragePricePerShare() {
        return averagePricePerShare;
    }

    public void setAveragePricePerShare(BigDecimal averagePricesPerShare) {
        this.averagePricePerShare = averagePricesPerShare;
    }
    
}

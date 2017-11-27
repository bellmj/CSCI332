/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bellcsci332.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthew Bell
 */
public class Portfolio implements Serializable {
    private String ownerEmail;
    private List<SimplePortfolioHolding> portfolioHoldings;

    public Portfolio() {
        portfolioHoldings = new ArrayList<>();
    }

    public Portfolio(String ownerEmail, List<SimplePortfolioHolding> portfolioHoldings) {
        this.ownerEmail = ownerEmail;
        this.portfolioHoldings = portfolioHoldings;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public List<SimplePortfolioHolding> getPortfolioHoldings() {
        return portfolioHoldings;
    }

    public void setPortfolioHoldings(List<SimplePortfolioHolding> portfolioHoldings) {
        this.portfolioHoldings = portfolioHoldings;
    }
    public void addPortfolioHolding(SimplePortfolioHolding holding){
        this.portfolioHoldings.add(holding);
    }
    
    
}

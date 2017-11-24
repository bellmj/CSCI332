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
public class CompanyInfo implements Serializable {
    private String symbol;
    private String name;
    private String sector;
    private String industry;

    public CompanyInfo() {
    }

    public CompanyInfo(String symbol, String name, String sector, String industry) {
        this.symbol = symbol;
        this.name = name;
        this.sector = sector;
        this.industry = industry;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
}

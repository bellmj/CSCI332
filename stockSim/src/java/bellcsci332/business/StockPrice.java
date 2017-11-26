/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bellcsci332.business;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Matthew Bell
 */
public class StockPrice implements Serializable,Comparable{
    private String symbol;
    private Timestamp timeStamp;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private int volume;

    public StockPrice() {
    }

    public StockPrice(String symbol, Timestamp timeStamp, BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low,int volume) {
        this.symbol = symbol;
        this.timeStamp = timeStamp;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    @Override
    public int compareTo(Object o) {
        long result = this.timeStamp.getTime() - ((StockPrice)o).getTimeStamp().getTime();
        if(result < 0){
            return -1;
        }else if(result > 0){
            return 1;
        }else {
            return 0;
        }
    }
    
    
    
}

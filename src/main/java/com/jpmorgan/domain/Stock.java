package com.jpmorgan.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 * Created by Sougata Bhattacharjee
 * On 10.03.18
 */
@Entity
public class Stock {

    @Id
    private String symbol;
    private double lastDividend;
    private double fixedDividend;
    private double parValue;
    private double marketPrice;
    @Enumerated(EnumType.STRING)
    private StockType type;

    public Stock() {
    }

    public Stock(final String symbol, final double lastDividend, final double fixedDividend,
                 final double parValue, final double marketPrice, final StockType type) {
        this.symbol = symbol;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
        this.marketPrice = marketPrice;
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public double getLastDividend() {
        return lastDividend;
    }

    public void setLastDividend(final double lastDividend) {
        this.lastDividend = lastDividend;
    }

    public double getFixedDividend() {
        return fixedDividend;
    }

    public void setFixedDividend(final double fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public double getParValue() {
        return parValue;
    }

    public void setParValue(final double parValue) {
        this.parValue = parValue;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(final double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public StockType getType() {
        return type;
    }

    public void setType(final StockType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stock stock = (Stock) o;

        if (Double.compare(stock.lastDividend, lastDividend) != 0) return false;
        if (Double.compare(stock.fixedDividend, fixedDividend) != 0) return false;
        if (Double.compare(stock.parValue, parValue) != 0) return false;
        if (Double.compare(stock.marketPrice, marketPrice) != 0) return false;
        if (!symbol.equals(stock.symbol)) return false;
        return type == stock.type;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = symbol.hashCode();
        temp = Double.doubleToLongBits(lastDividend);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(fixedDividend);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(parValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(marketPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", lastDividend=" + lastDividend +
                ", fixedDividend=" + fixedDividend +
                ", parValue=" + parValue +
                ", marketPrice=" + marketPrice +
                ", type=" + type +
                '}';
    }
}

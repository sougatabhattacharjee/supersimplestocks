package com.jpmorgan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by Sougata Bhattacharjee
 * On 10.03.18
 */
@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tradingTime;

    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @ManyToOne
    private Stock stock;

    private double tradePrice;

    public Trade() {
    }

    public Trade(final Date tradingTime, final int stockQuantity,
                 final TradeType tradeType, final Stock stock, final double tradePrice) {
        this.tradingTime = tradingTime;
        this.stockQuantity = stockQuantity;
        this.tradeType = tradeType;
        this.stock = stock;
        this.tradePrice = tradePrice;
    }

    public Date getTradingTime() {
        return tradingTime;
    }

    public void setTradingTime(final Date tradingTime) {
        this.tradingTime = tradingTime;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(final int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(final TradeType tradeType) {
        this.tradeType = tradeType;
    }

    @JsonProperty("stock")
    public Stock getStock() {
        return stock;
    }

    public void setStock(final Stock stock) {
        this.stock = stock;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(final double tradePrice) {
        this.tradePrice = tradePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trade trade = (Trade) o;

        if (stockQuantity != trade.stockQuantity) return false;
        if (Double.compare(trade.tradePrice, tradePrice) != 0) return false;
        if (!tradingTime.equals(trade.tradingTime)) return false;
        if (tradeType != trade.tradeType) return false;
        return stock.equals(trade.stock);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = tradingTime.hashCode();
        result = 31 * result + stockQuantity;
        result = 31 * result + tradeType.hashCode();
        result = 31 * result + stock.hashCode();
        temp = Double.doubleToLongBits(tradePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradingTime=" + tradingTime +
                ", stockQuantity=" + stockQuantity +
                ", tradeType=" + tradeType +
                ", stock=" + stock +
                ", tradePrice=" + tradePrice +
                '}';
    }
}

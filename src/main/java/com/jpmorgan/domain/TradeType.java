package com.jpmorgan.domain;

/**
 * Created by Sougata Bhattacharjee
 * On 10.03.18
 */
public enum TradeType {
    BUY, SELL;

    public String toString() {
        return this.name();
    }
}

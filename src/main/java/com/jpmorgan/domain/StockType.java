package com.jpmorgan.domain;

/**
 * Created by Sougata Bhattacharjee
 * On 10.03.18
 */
public enum StockType {
    COMMON("Common"), PREFERRED("Preferred");

    private final String type;

    private StockType(final String type) {
        this.type = type;
    }

    public boolean equalsName(final String otherName) {
        return type.equals(otherName);
    }

    public String toString() {
        return this.type;
    }
}

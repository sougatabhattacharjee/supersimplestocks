package com.jpmorgan.service.simpleStock;

import org.springframework.stereotype.Service;

/**
 * Created by Sougata Bhattacharjee
 * On 11.03.18
 */
@Service
public interface SimpleStockService {

    /**
     * Method used to calculate the dividend yield for a stock is given by the following formula:
     * If the given stock is a common stock, the dividend yield = (last dividend / the stock price).
     * If the given stock is a preferred stock, the dividend yield = (fix dividend * par value / the stock price).
     *
     * @param stockSymbol the given stock symbol
     * @return the dividend yield
     */
    double calculateDividendYield(String stockSymbol);

    /**
     * Method used to calculate the PE ratio by ithe formula - (stock price / the dividend yield)
     *
     * @param stockSymbol the given stock symbol
     * @return the PE ratio for the stock
     */
    double calculatePERatio(String stockSymbol);

    /**
     * Method used to calculate the GBCE index is given by the geometric mean of prices for all
     * stocks.
     *
     * @return GECEALL Share index
     */
    double calculateGBCEAllShareIndex();

    /**
     * Method used to calculate weighted stock price for a given stock.
     * The price should be calculated based on trades in past 15 minutes only.
     *
     * @param stockSymbol the given stock symbol
     * @return the weighted stock price
     */
    double getWeightedStockPrice(String stockSymbol);

}

package com.jpmorgan.service.simpleStock;

import com.google.common.collect.Lists;
import com.jpmorgan.domain.Stock;
import com.jpmorgan.domain.Trade;
import com.jpmorgan.exception.PriceNotFoundException;
import com.jpmorgan.exception.StockNotFoundException;
import com.jpmorgan.exception.TradeNotFoundException;
import com.jpmorgan.service.stock.StockService;
import com.jpmorgan.service.trade.TradeService;
import com.jpmorgan.util.DateTimeUtil;
import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.jpmorgan.exception.StockNotFoundException.MSG_STOCK_NOT_FOUND;
import static com.jpmorgan.util.NumberUtil.round;
import static com.jpmorgan.util.NumberUtil.roundOffTo2DecPlaces;

/**
 * Created by Sougata Bhattacharjee
 * On 11.03.18
 */

public class SimpleStockServiceImpl implements SimpleStockService {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleStockServiceImpl.class);

    @Autowired
    private StockService stockService;

    @Autowired
    private TradeService tradeService;

    @Override
    public double calculateDividendYield(final String stockSymbol) {
        final Stock stock = stockService.findStockBySymbol(stockSymbol);
        final double marketPrice = checkStockPrice(stock);

        switch (stock.getType()) {
            case COMMON:
                return roundOffTo2DecPlaces(stock.getLastDividend() / marketPrice);
            case PREFERRED:
                return roundOffTo2DecPlaces((stock.getFixedDividend() * stock.getParValue()) / marketPrice);
            default:
                throw new RuntimeException("Unknown stock type");
        }
    }

    @Override
    public double calculatePERatio(final String stockSymbol) {
        final Stock stock = stockService.findStockBySymbol(stockSymbol);
        final double dividendYield = calculateDividendYield(stockSymbol);
        if (dividendYield <= 0d) throw new ArithmeticException("Dividend cannot be less or equal to zero");
        return roundOffTo2DecPlaces(checkStockPrice(stock) / dividendYield);
    }

    @Override
    public double getWeightedStockPrice(final String stockSymbol) throws RuntimeException {

        // Consider trades only for last 15 minutes
        final List<Trade> trades = getTradesLastWithin15Minutes(stockSymbol);

        LOG.info("Trading lists {}", trades);

        if (CollectionUtils.isEmpty(trades)) {
            throw new TradeNotFoundException(String.format("Trade record not found with the stock %s", stockSymbol));
        } else {
            final double sumOfTradePriceWithQuantity =
                    trades.stream().
                            mapToDouble(trade -> trade.getTradePrice() * trade.getStockQuantity())
                            .sum();

            final double sumOfStockQuantityWithinTrades = trades.stream().mapToDouble(Trade::getStockQuantity).sum();

            double weightedStockPrice = roundOffTo2DecPlaces(sumOfTradePriceWithQuantity / sumOfStockQuantityWithinTrades);

            // this is an assumption that Stock price should be updated with the weighted price
            final Stock stock = stockService.findStockBySymbol(stockSymbol);
            stock.setMarketPrice(weightedStockPrice);
            stockService.saveStock(stock);

            return weightedStockPrice;
        }
    }

    @Override
    public double calculateGBCEAllShareIndex() {
        final List<Stock> stocks = Lists.newArrayList(stockService.findAllStocks());

        if (CollectionUtils.isEmpty(stocks))
            throw new StockNotFoundException(MSG_STOCK_NOT_FOUND);

        return round(StatUtils.geometricMean(stocks.stream().mapToDouble(Stock::getMarketPrice).toArray()), 4);
    }

    /**
     * Get list of trades by stock symbol for last 15 minutes
     * @param stockSymbol stock for which trades to be searched
     * @return the trades
     */
    private List<Trade> getTradesLastWithin15Minutes(final String stockSymbol) {

        return tradeService.findTradesByStockSymbol(stockSymbol).stream().filter(trade ->
                trade.getTradingTime().compareTo(DateTimeUtil.getMinutesBefore(15)) > 0
        ).collect(Collectors.toList());
    }

    /**
     * Check if the stock price is not less than or equal to zero
     * @param stock price
     * @return the price
     */
    private double checkStockPrice(final Stock stock) {
        if (stock.getMarketPrice() <= 0)
            throw new PriceNotFoundException("Price cannot be less than or equal to zero");
        return stock.getMarketPrice();
    }

}

package com.jpmorgan.service.trade;

import com.google.common.collect.Lists;
import com.jpmorgan.domain.Stock;
import com.jpmorgan.domain.Trade;
import com.jpmorgan.domain.TradeType;
import com.jpmorgan.exception.TradeNotFoundException;
import com.jpmorgan.exception.TradingException;
import com.jpmorgan.repository.TradeRepository;
import com.jpmorgan.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static com.jpmorgan.exception.TradeNotFoundException.MSG_TRADE_NOT_FOUND;

/**
 * Created by Sougata Bhattacharjee
 * On 16.03.18
 */
public class TradeServiceImpl implements TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private StockService stockService;

    @Override
    public Trade recordTrade(final String stockSymbol, final int quantity,
                             final TradeType tradeType, final double price) {
        final Stock stock = stockService.findStockBySymbol(stockSymbol);

        if (quantity <= 0)
            throw new TradingException("Shares quantity should be greater than zero while trading");

        if (price <= 0.0)
            throw new TradingException("Shares price should be greater than zero while trading");

        final Trade trade = new Trade(new Date(), quantity, tradeType, stock, price);
        tradeRepository.save(trade);

        // this is an assumption that Stock price can be updated by the latest trade price
        stock.setMarketPrice(price);
        stockService.saveStock(stock);

        return trade;
    }

    @Override
    public List<Trade> findAllTrades() {
        final List<Trade> trades = Lists.newArrayList(tradeRepository.findAll());
        if (trades.isEmpty()) throw new TradeNotFoundException(MSG_TRADE_NOT_FOUND);
        return trades;
    }

    @Override
    public void deleteAllTrades() {
        tradeRepository.deleteAll();
    }

    @Override
    public List<Trade> findTradesByStockSymbol(String stockSymbol) {
        final List<Trade> trades = tradeRepository.findByStockSymbol(stockSymbol);
        if (trades.isEmpty()) throw new TradeNotFoundException(MSG_TRADE_NOT_FOUND);
        return trades;
    }

}

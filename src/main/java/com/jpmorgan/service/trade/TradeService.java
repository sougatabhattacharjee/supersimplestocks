package com.jpmorgan.service.trade;

import com.jpmorgan.domain.Trade;
import com.jpmorgan.domain.TradeType;

import java.util.List;

/**
 * Created by Sougata Bhattacharjee
 * On 16.03.18
 */
public interface TradeService {

    Trade recordTrade(String stockSymbol, int quantity, TradeType tradeType, double price);

    List<Trade> findAllTrades();

    void deleteAllTrades();

    List<Trade> findTradesByStockSymbol(String stockSymbol);

}

package com.jpmorgan.service.stock;

import com.jpmorgan.domain.Stock;

import java.util.List;

/**
 * Created by Sougata Bhattacharjee
 * On 16.03.18
 */
public interface StockService {

    List<Stock> saveStocks(List<Stock> stocks);

    Stock saveStock(Stock stock);

    List<Stock> findAllStocks();

    Stock findStockBySymbol(String stockSymbol);

    void deleteAllStocks();

}

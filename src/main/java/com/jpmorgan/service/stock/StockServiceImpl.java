package com.jpmorgan.service.stock;

import com.google.common.collect.Lists;
import com.jpmorgan.domain.Stock;
import com.jpmorgan.exception.StockNotFoundException;
import com.jpmorgan.repository.StockRepository;
import com.jpmorgan.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static com.jpmorgan.exception.StockNotFoundException.MSG_STOCK_NOT_FOUND;

/**
 * Created by Sougata Bhattacharjee
 * On 16.03.18
 */
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public List<Stock> saveStocks(final List<Stock> stocks) {
        final List<Stock> validStocks = validateStocks(stocks);
        validStocks.forEach(
                stock -> stockRepository.save(stock)
        );

        return validStocks;
    }

    @Override
    public Stock saveStock(final Stock stock) {
        if (stock == null) throw new StockNotFoundException(MSG_STOCK_NOT_FOUND);
        final Stock validatedStock = validateStocks(Lists.newArrayList(stock)).get(0);
        return stockRepository.save(validatedStock);
    }

    @Override
    public List<Stock> findAllStocks() {
        final List<Stock> stocks = Lists.newArrayList(stockRepository.findAll());
        if (stocks.isEmpty()) throw new StockNotFoundException(MSG_STOCK_NOT_FOUND);
        return stocks;
    }

    @Override
    public Stock findStockBySymbol(final String stockSymbol) {
        return stockRepository.findBySymbol(stockSymbol).
                orElseThrow(() -> new StockNotFoundException(MSG_STOCK_NOT_FOUND));
    }

    @Override
    public void deleteAllStocks() {
        stockRepository.deleteAll();
    }

    /**
     * Validate a stock data.
     * If the stock price is not greater than zero then set a random integer to the price.
     * @param stocks list of stock data to be validated
     * @return validated stocks
     */
    private List<Stock> validateStocks(final List<Stock> stocks) {
        return stocks.stream().peek(stock -> {
            if (stock.getMarketPrice() <= 0.0)
                stock.setMarketPrice(NumberUtil.randDouble());

        }).collect(Collectors.toList());
    }
}

package com.jpmorgan.config;

import com.jpmorgan.service.simpleStock.SimpleStockService;
import com.jpmorgan.service.simpleStock.SimpleStockServiceImpl;
import com.jpmorgan.service.stock.StockService;
import com.jpmorgan.service.stock.StockServiceImpl;
import com.jpmorgan.service.trade.TradeService;
import com.jpmorgan.service.trade.TradeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Sougata Bhattacharjee
 * On 11.03.18
 */
@Configuration
public class AppConfig {

    @Bean
    SimpleStockService simpleStockService() {
        return new SimpleStockServiceImpl();
    }

    @Bean
    TradeService tradeService() {
        return new TradeServiceImpl();
    }

    @Bean
    StockService stockService() {
        return new StockServiceImpl();
    }
}

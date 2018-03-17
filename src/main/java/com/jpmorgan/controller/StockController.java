package com.jpmorgan.controller;

import com.google.common.collect.Lists;
import com.jpmorgan.domain.Stock;
import com.jpmorgan.service.stock.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Created by Sougata Bhattacharjee
 * On 10.03.18
 */
@RestController
@RequestMapping(value = StockController.STOCK_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class StockController {

    private static final Logger LOG = LoggerFactory.getLogger(StockController.class);
    public static final String STOCK_URL = "/stock";

    @Autowired
    private StockService stockService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Stock>> saveStock(@RequestBody final List<Stock> stocks) {
        LOG.info("saving {} stocks {}", stocks.size(), stocks);
        return ok().body(stockService.saveStocks(stocks));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Stock>> getStocks() {
        return ok().body(Lists.newArrayList(stockService.findAllStocks()));
    }

    @RequestMapping(path = "/{symbol}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Stock> getBySymbol(@PathVariable(value = "symbol") final String symbol) {
        return ok().body(stockService.findStockBySymbol(symbol));
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Stock>> deleteAllStock() {
        stockService.deleteAllStocks();
        return ok().body(Collections.emptyList());
    }

}

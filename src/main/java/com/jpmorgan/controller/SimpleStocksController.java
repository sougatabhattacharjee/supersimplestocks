package com.jpmorgan.controller;

import com.jpmorgan.exception.ResponseError;
import com.jpmorgan.exception.StockNotFoundException;
import com.jpmorgan.exception.TradeNotFoundException;
import com.jpmorgan.service.simpleStock.SimpleStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.jpmorgan.util.Constants.PARAM_STOCK_NAME;
import static org.springframework.http.ResponseEntity.status;

/**
 * Created by Sougata Bhattacharjee
 * On 11.03.18
 */
@RestController
@RequestMapping(value = SimpleStocksController.SUPER_SIMPLE_STOCKS_URL,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class SimpleStocksController {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleStocksController.class);

    public static final String SUPER_SIMPLE_STOCKS_URL = "/super-simple-stocks";

    @Autowired
    public SimpleStockService simpleStockService;

    @RequestMapping(value = "/calculateDividendYield", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity calculateDividend(@RequestParam(value = PARAM_STOCK_NAME) final String stock) {

        try {
            return status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).
                    body(simpleStockService.calculateDividendYield(stock));
        } catch (final StockNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(ex));
        } catch (final Exception ex) {
            LOG.error("Exception occurred while calculating Dividend for {}, - {}", stock, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(ex));
        }
    }

    @RequestMapping(value = "/calculatePERatio", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity calculatePERatio(@RequestParam(value = PARAM_STOCK_NAME) final String stock) {

        try {
            return status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).
                    body(simpleStockService.calculatePERatio(stock));
        } catch (final StockNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(ex));
        } catch (final Exception ex) {
            LOG.error("Exception occurred while calculating PERatio for {}, - {}", stock, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(ex));
        }
    }

    @RequestMapping(value = "/weightedStockPrice", method = RequestMethod.GET)
    public ResponseEntity calculateWeightedStockPrice(@RequestParam(value = PARAM_STOCK_NAME) final String stock) {

        final double weightedStockPrice = simpleStockService.getWeightedStockPrice(stock);
        try {
            return status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).
                    body(weightedStockPrice);
        } catch (final TradeNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(ex));
        } catch (final Exception ex) {
            LOG.error("Exception occurred while calculating Weighted stock price" +
                    " for {}, - {}", stock, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(ex));
        }
    }

    @RequestMapping(value = "/calculateGBCEAllShareIndex", method = RequestMethod.GET)
    public ResponseEntity calculateGBCEAllShareIndex() {

        try {
            return status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).
                    body(simpleStockService.calculateGBCEAllShareIndex());
        } catch (final StockNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(ex));
        } catch (final Exception ex) {
            LOG.error("Exception occurred while calculating GBCEAllShareIndex - {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(ex));
        }
    }

}


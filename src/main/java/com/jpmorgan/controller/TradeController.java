package com.jpmorgan.controller;

import com.google.common.collect.Lists;
import com.jpmorgan.domain.Trade;
import com.jpmorgan.domain.TradeType;
import com.jpmorgan.exception.ResponseError;
import com.jpmorgan.exception.StockNotFoundException;
import com.jpmorgan.service.trade.TradeService;
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

import java.util.Collections;
import java.util.List;

import static com.jpmorgan.util.Constants.PARAM_SHARE_QUANTITY;
import static com.jpmorgan.util.Constants.PARAM_STOCK_NAME;
import static com.jpmorgan.util.Constants.PARAM_TRADE_PRICE;
import static com.jpmorgan.util.Constants.PARAM_TRADE_TYPE;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

/**
 * Created by Sougata Bhattacharjee
 * On 16.03.18
 */
@RestController
@RequestMapping(value = TradeController.TRADE_URL,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class TradeController {

    private static final Logger LOG = LoggerFactory.getLogger(TradeController.class);

    public static final String TRADE_URL = "/trade";

    @Autowired
    public TradeService tradeService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity trading(@RequestParam(value = PARAM_STOCK_NAME) final String stock,
                                  @RequestParam(value = PARAM_SHARE_QUANTITY) final int quantity,
                                  @RequestParam(value = PARAM_TRADE_TYPE) final TradeType tradeType,
                                  @RequestParam(value = PARAM_TRADE_PRICE, required = false) final double price) {

        final Trade trade = tradeService.recordTrade(stock, quantity, tradeType, price);
        try {
            return status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).
                    body(trade);
        } catch (final StockNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(ex));
        } catch (final Exception ex) {
            LOG.error("Exception occurred while trading - {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(ex));
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Trade>> getTrades() {
        return ok().body(Lists.newArrayList(tradeService.findAllTrades()));
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Trade>> deleteAllTrades() {
        tradeService.deleteAllTrades();
        return ok().body(Collections.emptyList());
    }
}

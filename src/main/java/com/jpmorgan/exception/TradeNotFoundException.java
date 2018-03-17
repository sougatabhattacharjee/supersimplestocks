package com.jpmorgan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Sougata Bhattacharjee
 * On 11.03.18
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TradeNotFoundException extends RuntimeException {

    public static final String MSG_TRADE_NOT_FOUND = "Trade Not Found!";

    public TradeNotFoundException(final String message) {
        super(message);
    }

    public TradeNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

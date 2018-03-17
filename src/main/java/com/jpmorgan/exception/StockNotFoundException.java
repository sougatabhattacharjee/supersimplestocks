package com.jpmorgan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Sougata Bhattacharjee
 * On 11.03.18
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StockNotFoundException extends RuntimeException {

    public static final String MSG_STOCK_NOT_FOUND = "Stock Not Found!";

    public StockNotFoundException(final String message) {
        super(message);
    }

    public StockNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

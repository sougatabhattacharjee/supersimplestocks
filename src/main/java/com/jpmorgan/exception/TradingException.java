package com.jpmorgan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Sougata Bhattacharjee
 * On 11.03.18
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TradingException extends RuntimeException {

    public TradingException(final String message) {
        super(message);
    }

    public TradingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

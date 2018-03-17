package com.jpmorgan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Sougata Bhattacharjee
 * On 11.03.18
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PriceNotFoundException extends RuntimeException {

    private static final String MSG_PRICE_NOT_FOUND = "Price Not Found!";

    public PriceNotFoundException(final String message) {
        super(message);
    }

    public PriceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

package com.jpmorgan.exception;

/**
 * Created by Sougata Bhattacharjee
 * On 11.03.18
 */
public class ResponseError {
    private String message;

    public ResponseError(final String message, final String... args) {
        this.message = String.format(message, args);
    }

    public ResponseError(final Exception e) {
        this.message = e.getMessage();
    }

    public String getMessage() {
        return this.message;
    }
}

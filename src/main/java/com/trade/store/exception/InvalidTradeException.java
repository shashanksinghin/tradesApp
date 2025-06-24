package com.trade.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to indicate invalid trade data or business rule violations.
 * This will result in an HTTP 400 Bad Request status if thrown from a REST controller.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTradeException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTradeException(String message) {
        super(message);
    }
}
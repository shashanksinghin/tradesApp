package com.trade.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * It is a spring boot application for trade store app.
 * @author shashank
 *
 */
@SpringBootApplication
public class TradeApplication {

	/**
	 * The main method is actually entry point of application.
	 * @param args command line arguments
	 */
	public static void main(final String[] args) {
		SpringApplication.run(TradeApplication.class, args);
	}

}

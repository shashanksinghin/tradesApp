package com.trade.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trade.store.kafka.TradeProducer;
import com.trade.store.model.Trade;

/**
 * The controller will expose a rest API to put messages in the trade Kafka
 * topic.
 * 
 * @author shashank
 *
 */
@RestController
@RequestMapping("/trades")
public class TradeController {

	/**
	 * This producer will push the trade to kafka topic
	 */
	private TradeProducer tradeProducer;

	
	public TradeController(TradeProducer tradeProducer) {
		this.tradeProducer = tradeProducer;
	}
	/**
	 * a POST API which will read the trade and pass on to kafka topic.
	 * 
	 * @param trade
	 * @return
	 */
	@PostMapping
	public ResponseEntity<String> sendTrade(final @RequestBody Trade trade) {
		tradeProducer.sendTrade(trade);
		return ResponseEntity.ok("Trade sent for processing.");
	}
}

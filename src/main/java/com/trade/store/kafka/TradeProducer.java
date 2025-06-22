package com.trade.store.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.trade.store.model.Trade;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author shashank
 *
 */
@Service
@RequiredArgsConstructor
public class TradeProducer {
	
	/**
	 * The topic name which is configurable via properties file.
	 */
	@Value("${trade.store.topic}")
	private String tradeStoreTopic;

	/**
	 * 
	 */
	private final KafkaTemplate<String, Trade> kafkaTemplate;
	
	/**
	 * 
	 * @param trade
	 */
	public void sendTrade(final Trade trade) {
		kafkaTemplate.send(tradeStoreTopic, trade);
	}
}

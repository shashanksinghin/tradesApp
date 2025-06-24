package com.trade.store.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.trade.store.model.Trade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author shashank
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TradeProducer {
	
	/**
	 * The topic name which is configurable via properties file.
	 */
	@Value("${trade.store.topic}")
	private String tradeStoreTopic; // Kafka topic for incoming trades

	/**
	 * 
	 */
	private final KafkaTemplate<String, Trade> kafkaTemplate;
	
	/**
	 * Sends a Trade object to the Kafka topic.
     * The tradeId is used as the message key to ensure ordered processing for the same trade ID.
	 * @param trade trade The Trade object to send.
	 */
	public void sendTrade(final Trade trade) {
		log.info("Sending trade {} to topic {}", trade.getTradeId(), tradeStoreTopic);
		kafkaTemplate.send(tradeStoreTopic, trade.getTradeId(), trade)
		.whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("Sent message successfully for trade ID: {} with offset: {}", trade.getTradeId(), result.getRecordMetadata().offset());
			} else {
				log.error("Failed to send message for trade ID: {} due to: {}", trade.getTradeId(), ex.getMessage());
			}
		});
	}
}

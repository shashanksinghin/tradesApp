package com.trade.store.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.trade.store.exception.InvalidTradeException;
import com.trade.store.model.Trade;
import com.trade.store.service.TradeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka Consumer to listen for incoming Trade messages from a Kafka topic.
 * Upon receiving a trade, it delegates to the TradeService for processing.
 * @author shashank
 *
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TradeConsumer {
	
	/**
	 * TradeService
	 */
	private final TradeService tradeService;

	/**
	 * 
	 * @param trade
	 */
	@KafkaListener(topics = "${trade.store.topic}", groupId = "${trade.store.group}")
	public void consumeTrade(final Trade trade) {
		log.info("Received trade from Kafka: {}", trade.getTradeId());
		try {
			tradeService.processTrade(trade);
			log.info("Successfully processed and saved trade: {}", trade.getTradeId());
		} catch (InvalidTradeException e) {
			log.error("Failed to process trade {} due to validation error: {}", trade.getTradeId(), e.getMessage());
			// Depending on requirements, could send to a dead-letter topic here
		} catch (Exception e) {
			log.error("An unexpected error occurred while processing trade {}: {}", trade.getTradeId(), e.getMessage());
			// Handle other exceptions
		}
	}
}

package com.trade.store.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.trade.store.model.Trade;
import com.trade.store.service.TradeService;

import lombok.RequiredArgsConstructor;

/**
 * It is a kafka consumer class which will consume the trade received on kafka topic 
 * @author shashank
 *
 */
@Service
@RequiredArgsConstructor
public class TradeConsumer {
	
	/**
	 * 
	 */
	private final TradeService tradeService;

	/**
	 * 
	 * @param trade
	 */
	@KafkaListener(topics = "${trade.store.topic}", groupId = "${trade.store.group}")
	public void consumeTrade(final Trade trade) {
		tradeService.processTrade(trade);
	}
}

package com.trade.store.kafka;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trade.store.model.Trade;
import com.trade.store.service.TradeService;

/**
 * JUnit test case for TradeConsumer class
 * @author shashank
 *
 */
class TradeConsumerTest {

	/**
	 * TradeService
	 */
	@Mock
	private TradeService tradeService;

	/**
	 * TradeConsumer
	 */
	@InjectMocks
	private TradeConsumer tradeConsumer;

	/**
	 * TradeConsumerTest constructor
	 */
	public TradeConsumerTest() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * 
	 */
	@Test
	void testConsumeTrade() {
		final Trade trade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now(), false);
		tradeConsumer.consumeTrade(trade);
		verify(tradeService, times(1)).processTrade(trade);
	}


}

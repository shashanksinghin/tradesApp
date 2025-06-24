package com.trade.store.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trade.store.exception.InvalidTradeException;
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
	
	/**
	 * 
	 */
	@Test
	void testConsumeTradeWithException() {
		final Trade expiredTrade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().minusDays(10), LocalDate.now(), false);
		
		doThrow(new InvalidTradeException("Trade maturity date has passed!")).when(tradeService).processTrade(expiredTrade);
		
		final Exception exception = assertThrows(InvalidTradeException.class, () -> tradeConsumer.consumeTrade(expiredTrade));
		assertEquals("Trade maturity date has passed!", exception.getMessage(), "Trade maturity date failed.");
	}


}

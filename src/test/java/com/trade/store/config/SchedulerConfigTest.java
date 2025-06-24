package com.trade.store.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trade.store.model.Trade;
import com.trade.store.repository.TradeRepository;
import com.trade.store.service.TradeService;

class SchedulerConfigTest {
	
	/**
	 * TradeService
	 */
	@Mock
	private TradeService tradeService;
	
	/**
	 * TradeRepository
	 */
	@Mock
	private TradeRepository tradeRepository; // Mock the repository dependency
	
	/**
	 * SchedulerConfig
	 */
	@InjectMocks
	private SchedulerConfig schedulerConfig; // Inject mocks into the service under test
	
	
	/**
	 * SchedulerConfigTest constructor
	 */
	public SchedulerConfigTest() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * 
	 */
	@Test
	void testUpdateExpiredTradesScheduler() {
		
		 // Given: A trade that has not matured (maturity date in the future)
        Trade nonMaturedTrade = new Trade("T6", 1, "CP-2", "B2", LocalDate.now().plusDays(10), LocalDate.now(), false);

        when(tradeRepository.findAll()).thenReturn(Collections.singletonList(nonMaturedTrade));
        when(tradeRepository.save(any(Trade.class))).thenReturn(nonMaturedTrade);
		
		schedulerConfig.updateExpiredTradesScheduler();
		
		// Then
        // Verify that save was never called for this trade
        verify(tradeRepository, never()).save(any(Trade.class));
	}

}

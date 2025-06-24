package com.trade.store.config;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trade.store.service.TradeService;

class SchedulerConfigTest {
	
	/**
	 * TradeService
	 */
	@Mock
	private TradeService tradeService;
	
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
		schedulerConfig.updateExpiredTradesScheduler();
	}

}

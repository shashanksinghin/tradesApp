package com.trade.store.config;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class KafkaConfigTest {
	
	
	/**
	 * kafkaConfig
	 */
	@InjectMocks
	private KafkaConfig kafkaConfig; // Inject mocks into the service under test

	
	/**
	 * KafkaConfigTest constructor
	 */
	public KafkaConfigTest() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testTradeTopic() {
		kafkaConfig.tradeTopic();
	}

}

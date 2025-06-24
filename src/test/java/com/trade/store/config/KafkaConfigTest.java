package com.trade.store.config;

import static org.junit.Assert.assertNotNull;

import org.apache.kafka.clients.admin.NewTopic;
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
		NewTopic newTopic = kafkaConfig.tradeTopic();
		assertNotNull(newTopic);
	}

}

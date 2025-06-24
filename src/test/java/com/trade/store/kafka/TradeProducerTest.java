package com.trade.store.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import com.trade.store.model.Trade;

/**
 * JUnit test case for TradeProducer class
 * @author shashank
 *
 */
class TradeProducerTest {
	
	/**
	 * the topic
	 */
	private final String topic = "my-topic";

	/**
	 * KafkaTemplate
	 */
	@Mock
	private KafkaTemplate<String, Trade> kafkaTemplate;

	/**
	 * TradeProducer
	 */
	@InjectMocks
	private TradeProducer tradeProducer;
	
	/**
	 * 
	 */
	public TradeProducerTest() {
		System.setProperty("trade.store.topic", topic);
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * A test to send message on a kafka topic
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Test
	void testSendTrade() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		// Get the private field using reflection
		Field privateField = TradeProducer.class.getDeclaredField("tradeStoreTopic");
		privateField.setAccessible(true);

		// Populate the private variable
		privateField.set(tradeProducer, topic);


		final Trade trade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now(), false);
		CompletableFuture<SendResult<String, Trade>> completableFuture = new CompletableFuture<SendResult<String, Trade>>();

		when(kafkaTemplate.send(any(), any(), any())).thenReturn(completableFuture);

		tradeProducer.sendTrade(trade);

		// Verify the interaction with KafkaTemplate
		verify(kafkaTemplate).send(topic, trade.getTradeId(), trade);
	}

}

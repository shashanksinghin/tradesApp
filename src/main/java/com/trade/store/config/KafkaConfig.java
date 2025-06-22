package com.trade.store.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import lombok.NoArgsConstructor;

/**
 * A configuration file for Kafka.
 * @author shashank
 *
 */
@Configuration
@NoArgsConstructor
public class KafkaConfig {
	
	/**
	 * The topic name which is configurable via properties file.
	 */
	@Value("${trade.store.topic}")
	private String tradeStoreTopic;

	/**
	 * This bean will create a topic in kafka
	 * @return
	 */
	@Bean
	public NewTopic tradeTopic() {
		return TopicBuilder.name(tradeStoreTopic).partitions(3).replicas(1).build();
	}
}


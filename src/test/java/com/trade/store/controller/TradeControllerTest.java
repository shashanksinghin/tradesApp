package com.trade.store.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trade.store.kafka.TradeProducer;
import com.trade.store.model.Trade;

import lombok.NoArgsConstructor;

/**
 * 
 * @author shashank
 *
 */
@WebMvcTest(TradeController.class)
@NoArgsConstructor
class TradeControllerTest {

	/**
	 * 
	 */
	@Autowired
	private MockMvc mockMvc;

	/**
	 * 
	 */
	@MockitoBean
	private TradeProducer tradeProducer;

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	void testSendTrade() throws Exception {

		final Trade trade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now(), false);
		doNothing().when(tradeProducer).sendTrade(trade);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		final String jsonRequest = objectMapper.writeValueAsString(trade);

		mockMvc.perform(post("/trades") // Replace with your endpoint
				.contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isOk())
				.andExpect(content().string("Trade sent for processing."));
	}

}

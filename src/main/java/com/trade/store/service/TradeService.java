package com.trade.store.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trade.store.exception.InvalidTradeException;
import com.trade.store.model.Trade;
import com.trade.store.model.TradeAudit;
import com.trade.store.repository.TradeAuditRepository;
import com.trade.store.repository.TradeRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author shashank
 *
 */
@Service
@Slf4j // For logging
public class TradeService {
	/**
	 * 
	 */
	private final TradeRepository tradeRepository;
	
	/**
	 * 
	 */
	private final TradeAuditRepository tradeAuditRepository;

	/**
	 * 
	 * @param tradeRepository
	 * @param tradeAuditRepository
	 */
	public TradeService(final TradeRepository tradeRepository, final TradeAuditRepository tradeAuditRepository) {
		this.tradeRepository = tradeRepository;
		this.tradeAuditRepository = tradeAuditRepository;
	}

	/**
	 * 
	 * @param trade
	 */
	@KafkaListener(topics = "trade-store-topic", groupId = "trade-group")
	@Transactional
	public void processTrade(final Trade trade) {
		
		log.info("Attempting to save trade: {}", trade.getTradeId());

		//Store should not allow the trade which has less maturity date then today date.
		if (trade.getMaturityDate().isBefore(LocalDate.now())) {
			String errorMessage = "Trade maturity date has passed!";
			log.error(errorMessage);
			throw new InvalidTradeException(errorMessage);
		}

		//During transmission if the lower version is being received by the store it will reject 
		//the trade and throw an exception. If the version is same it will override the existing record.
		final List<Trade> existingTrades = tradeRepository.findByTradeId(trade.getTradeId());

		existingTrades.stream().max((t1, t2) -> Integer.compare(t1.getVersion(), t2.getVersion()))
		.ifPresent(existingTrade -> {
			if (trade.getVersion() < existingTrade.getVersion()) {
				
				String errorMessage = "Trade version is lower than existing record. Rejected.";
				log.error(errorMessage);
				throw new InvalidTradeException(errorMessage);
			}
		});

		tradeRepository.save(trade);
		tradeAuditRepository.save(new TradeAudit(trade.getTradeId()+trade.getVersion(),
				trade.getTradeId(), 
				trade.getVersion(), 
				trade.getCounterPartyId(), 
				trade.getBookId(), 
				trade.getMaturityDate(), 
				LocalDate.now(),
				"CREATED"));
	}

	/**
	 * A scheduler which should automatically update the expire flag if in a store the trade crosses the maturity date
	 */
	@Transactional
	public void updateExpiredTrades() {
		log.info("Starting scheduled task to update expired flags.");
		
		List<Trade> tradesList =  tradeRepository.findAll();
		tradesList.forEach(trade -> {
			if (trade.getMaturityDate().isBefore(LocalDate.now()) && !trade.getExpired()) {
				trade.setExpired(true);
				tradeRepository.save(trade);
			}
		});
		
		log.info("Finished updating expired flags.");
	}
}

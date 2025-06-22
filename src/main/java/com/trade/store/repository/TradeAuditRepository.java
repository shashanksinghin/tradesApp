package com.trade.store.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trade.store.model.TradeAudit;

/**
 * A mongo repository to store trade audit
 * @author shashank
 *
 */
public interface TradeAuditRepository extends MongoRepository<TradeAudit, Long> {
	/**
	 * This method will find audit entry document by tradeId.
	 * @param tradeId
	 * @return
	 */
	Optional<TradeAudit> findByTradeId(String tradeId);
}

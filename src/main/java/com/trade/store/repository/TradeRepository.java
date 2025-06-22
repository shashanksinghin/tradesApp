package com.trade.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trade.store.model.Trade;

/**
 * A jpa repository to store trade in RDBMS table
 * @author shashank
 *
 */
public interface TradeRepository extends JpaRepository<Trade, Long> {
	/**
	 * This method will find trade entity in the DB by trade id
	 * @param tradeId
	 * @return
	 */
	Optional<Trade> findByTradeId(String tradeId);
}

package com.trade.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trade.store.model.Trade;

/**
 * Spring Data JPA repository for Trade entities.
 * Provides standard CRUD operations and custom query methods.
 * @author shashank
 *
 */
public interface TradeRepository extends JpaRepository<Trade, Long> {
	/**
     * Finds all trades by a given trade ID.
     * In a real system, you might want to consider composite keys or unique constraints
     * if tradeId + version forms a unique key for active trades.
     * Here, we fetch all to handle versioning logic.
     * @param tradeId The ID of the trade.
     * @return A list of Trade objects matching the tradeId.
     */
	List<Trade> findByTradeId(String tradeId);
	
	/**
     * Finds a trade by its trade ID and version.
     * This is useful for directly retrieving a specific version of a trade.
     * @param tradeId The ID of the trade.
     * @param version The version of the trade.
     * @return An Optional containing the Trade if found, otherwise empty.
     */
    Optional<Trade> findByTradeIdAndVersion(String tradeId, Integer version);
}

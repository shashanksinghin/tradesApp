package com.trade.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trade.store.exception.InvalidTradeException;
import com.trade.store.model.Trade;
import com.trade.store.model.TradeAudit;
import com.trade.store.repository.TradeAuditRepository;
import com.trade.store.repository.TradeRepository;

import lombok.NoArgsConstructor;

/**
 * Unit tests for the TradeService.
 * These tests follow a TDD approach: they are written to fail initially
 * and will pass once the corresponding logic is implemented in TradeService.
 * @author shashank
 *
 */
@NoArgsConstructor
class TradeServiceTest {

	/**
	 * TradeRepository
	 */
	@Mock
	private TradeRepository tradeRepository; // Mock the repository dependency

	/**
	 * TradeAuditRepository
	 */
	@Mock
	private TradeAuditRepository tradeAuditRepository; // Mock the repository dependency

	/**
	 * 
	 */
	@InjectMocks
	private TradeService tradeService; // Inject mocks into the service under test
	
	private Trade existingTrade;
    private Trade newTrade;
    private Trade expiredTrade;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.existingTrade = new Trade("T2", 2, "CP-1", "B1", LocalDate.now(), LocalDate.of(2015, 3, 14), false);
		this.newTrade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now(), false);
		this.expiredTrade = new Trade("T3", 3, "CP-3", "B2", LocalDate.now().minusDays(1), LocalDate.now(), false);
	}

	/**
	 * Should successfully save a new trade if no existing trade is found
	 */
	@Test
	@DisplayName("Should successfully save a new trade if no existing trade is found")
	void testSaveNewTrade() {

		final TradeAudit tradeAudit = new TradeAudit(newTrade.getTradeId() + newTrade.getVersion(), 
				newTrade.getTradeId(),
				newTrade.getVersion(), 
				newTrade.getCounterPartyId(), 
				newTrade.getBookId(), 
				newTrade.getMaturityDate(),
				LocalDate.now(), 
				"CREATED");

		when(tradeRepository.findByTradeId("T1")).thenReturn(Collections.emptyList());
		when(tradeRepository.save(newTrade)).thenReturn(newTrade);
		when(tradeAuditRepository.save(tradeAudit)).thenReturn(tradeAudit);

		tradeService.processTrade(newTrade);
		verify(tradeRepository, times(1)).save(newTrade);
	}
	
	/**
	 * Should override existing trade if incoming version is same
	 */
	@Test
	@DisplayName("Should override existing trade if incoming version is same")
	void testReplaceSameVersionTrade() {

		final TradeAudit tradeAudit = new TradeAudit(existingTrade.getTradeId() + existingTrade.getVersion(), 
				existingTrade.getTradeId(),
				existingTrade.getVersion(), 
				existingTrade.getCounterPartyId(), 
				existingTrade.getBookId(), 
				existingTrade.getMaturityDate(),
				LocalDate.now(), 
				"CREATED");

		final Trade sameVersionTrade = new Trade("T2", 2, "CP-1", "B1", LocalDate.now(), LocalDate.of(2015, 3, 14), false);

		when(tradeRepository.findByTradeId("T2")).thenReturn(List.of(sameVersionTrade));
		when(tradeRepository.save(existingTrade)).thenReturn(existingTrade);
		when(tradeAuditRepository.save(tradeAudit)).thenReturn(tradeAudit);

		tradeService.processTrade(existingTrade);
		verify(tradeRepository, times(1)).save(sameVersionTrade);
	}
	
	/**
	 * Should reject trade if incoming version is lower than existing
	 */
	@Test
	@DisplayName("Should reject trade if incoming version is lower than existing")
	void testRejectLowerVersionTrade() {
		when(tradeRepository.findByTradeId("T2")).thenReturn(List.of(existingTrade));
//		when(tradeRepository.save(any())).thenReturn(Optional.empty());
//		when(tradeAuditRepository.save(any())).thenReturn(Optional.empty());

		final Trade lowerVersionTrade = new Trade("T2", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now(), false);
		final Exception exception = assertThrows(InvalidTradeException.class, () -> tradeService.processTrade(lowerVersionTrade));
		assertEquals("Trade version is lower than existing record. Rejected.", exception.getMessage(), "Trade version check failed.");
	}
	
	/**
	 * Should reject trade if maturity date is in the past
	 */
	@Test
	@DisplayName("Should test for maturity date check.")
	void testRejectTradeWithPastMaturityDate() {
		final Exception exception = assertThrows(InvalidTradeException.class, () -> tradeService.processTrade(expiredTrade));
		assertEquals("Trade maturity date has passed!", exception.getMessage(), "Trade maturity date failed.");
	}
	
	/**
	 * Should update expired flag for trades past their maturity date
	 */
	@Test
    @DisplayName("Should update expired flag for trades past their maturity date")
    void shouldUpdateExpiredFlagForMaturedTrades() {

        when(tradeRepository.findAll()).thenReturn(Collections.singletonList(expiredTrade));
        when(tradeRepository.save(any(Trade.class))).thenReturn(expiredTrade);

        // When
        tradeService.updateExpiredTrades();

        // Then
        // Verify that save was called with the updated trade
        verify(tradeRepository, times(1)).save(argThat(trade -> trade.getTradeId().equals("T3") && trade.getExpired()));
    }
	
	/**
	 * Should not update expired flag for trades not past their maturity date
	 */
	@Test
    @DisplayName("Should not update expired flag for trades not past their maturity date")
    void shouldNotUpdateExpiredFlagForNonMaturedTrades() {
        // Given: A trade that has not matured (maturity date in the future)
        Trade nonMaturedTrade = new Trade("T6", 1, "CP-2", "B2", LocalDate.now().plusDays(10), LocalDate.now(), false);

        when(tradeRepository.findAll()).thenReturn(Collections.singletonList(nonMaturedTrade));
        when(tradeRepository.save(any(Trade.class))).thenReturn(nonMaturedTrade);

        // When
        tradeService.updateExpiredTrades();

        // Then
        // Verify that save was never called for this trade
        verify(tradeRepository, never()).save(any(Trade.class));
    }

}

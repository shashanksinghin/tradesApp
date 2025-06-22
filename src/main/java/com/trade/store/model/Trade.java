package com.trade.store.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An entity bean for trade table in the trade store.
 * @author shashank
 *
 */
@Entity
@Table(name = "trades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

	/**
	 * Trade Id
	 */
	@Id
	@NotNull
	private String tradeId;

	/**
	 * Version
	 */
	@NotNull
	private Integer version;

	/**
	 * Counter-Party Id
	 */
	@NotNull
	private String counterPartyId;

	/**
	 * Book-Id
	 */
	@NotNull
	private String bookId;

	/**
	 * Maturity Date
	 */
	@NotNull
	@FutureOrPresent(message = "Maturity date must be today or in the future")
	private LocalDate maturityDate;

	/**
	 * Created Date
	 */
	private LocalDate createdDate = LocalDate.now();

	/**
	 * Expired
	 */
	private Boolean expired = false;
}

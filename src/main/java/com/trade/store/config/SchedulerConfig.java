package com.trade.store.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.trade.store.service.TradeService;

/**
 * Configuration class to enable Spring's scheduling capabilities.
 * It defines a scheduled task to update the 'expired' flag of trades.
 */
@Configuration
@EnableScheduling // Enables Spring's scheduled task execution
@Slf4j
public class SchedulerConfig {
	private final TradeService tradeService;
	
	// Inject cron expression from application.properties
    @Value("${trade.scheduler.cron}")
    private String tradeSchedulerCron;

    @Autowired
    public SchedulerConfig(TradeService tradeService) {
        this.tradeService = tradeService;
    }
    
    /**
     * Scheduled task to periodically update the 'expired' flag for trades
     * whose maturity date has passed.
     * The cron expression is configurable via 'trade.scheduler.cron' property.
     */
    @Scheduled(cron = "${trade.scheduler.cron}") // Uses cron expression from properties
    public void updateExpiredTradesScheduler() {
        log.info("Scheduled task 'updateExpiredTradesScheduler' triggered with cron: {}", tradeSchedulerCron);
        tradeService.updateExpiredTrades();
    }
}

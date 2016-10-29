package com.tradetraining.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.tradetraining.model.Tick;

public interface TickDataService {

	public List<Tick> getTicks(String symbol, LocalDateTime toDatetime, int noOfTicks, String period) throws SQLException;
	public Tick getAggregatedTick(String symbol, LocalDateTime fromDatetime, LocalDateTime toDatetime) throws SQLException;
	public double[] getBidAsk(String symbol, LocalDateTime datetime) throws SQLException;
	
}

package com.tradetraining.dao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.tradetraining.data.Tick;

public interface TickDataDao {

	public List<Tick> getTicks(String symbol, LocalDateTime toDatetime, int noOfTicks, String period) throws SQLException;
	public Tick getAggregatedTick(String symbol, LocalDateTime fromDatetime, LocalDateTime toDatetime) throws SQLException;
	public double[] getBidAsk(String symbol, LocalDateTime datetime) throws SQLException;
	
}

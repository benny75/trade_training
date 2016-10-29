package com.tradetraining.service;

import java.sql.SQLException;
import java.util.List;

import com.tradetraining.data.Trade;

public interface TradeService {

	public List<Trade> getTrades(Trade trade) throws SQLException;

	public double openTrade(Trade trade) throws SQLException, TradeUnexecutedException;

	public double closeTrade(Trade trade) throws SQLException;
	
	public double getOpeningTradesProfit(Trade trade) throws SQLException;
	
	public double getNetQuantity(Trade trade) throws SQLException;

}

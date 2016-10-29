package com.tradetraining.dao;

import java.sql.SQLException;
import java.util.List;

import com.tradetraining.data.Trade;

/**
 * @author Benny Wong
 * This interface will be used to communicate with the
 * Database
 */
public interface TradeDao
{
	public List<Trade> getTrades(Trade trade) throws SQLException;

	public boolean insertTrade(Trade trade) throws SQLException;

	public void closeTrade(Trade trade) throws SQLException;

	public double getOpeningTradesProfit(Trade trade) throws SQLException;

	public double getNetQuantity(Trade trade) throws SQLException;

	public double getOpeningTradesQuantity(Trade trade) throws SQLException;

}


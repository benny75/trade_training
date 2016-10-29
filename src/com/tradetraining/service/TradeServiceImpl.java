package com.tradetraining.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tradetraining.dao.TradeDao;
import com.tradetraining.model.Trade;

public class TradeServiceImpl implements TradeService{

	private TradeDao tradeDao;

	@Autowired
	private UserService userService;
	
	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	@Override
	public List<Trade> getTrades(Trade trade) throws SQLException {
		// TODO Auto-generated method stub
		return tradeDao.getTrades(trade);
	}

	@Override
	public double openTrade(Trade trade) throws SQLException, TradeUnexecutedException {
		if (tradeDao.insertTrade(trade)){
			double newBalance = userService.addAccountBalance(trade.getUser(), -1 * trade.getQuantity());
			return newBalance;
		} else {
			 throw new TradeUnexecutedException();
		}
	}

	@Override
	public double closeTrade(Trade trade) throws SQLException {
		double newBalance = userService.addAccountBalance(trade.getUser(), 
				tradeDao.getOpeningTradesQuantity(trade) + tradeDao.getOpeningTradesProfit(trade));
		tradeDao.closeTrade(trade);
		return newBalance;
	}

	@Override
	public double getOpeningTradesProfit(Trade trade) throws SQLException {
		return tradeDao.getOpeningTradesProfit(trade);
	}

	@Override
	public double getNetQuantity(Trade trade) throws SQLException {
		return tradeDao.getNetQuantity(trade);
	}
	
}

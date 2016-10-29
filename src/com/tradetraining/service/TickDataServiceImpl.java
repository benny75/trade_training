package com.tradetraining.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.tradetraining.dao.TickDataDao;
import com.tradetraining.model.Tick;

public class TickDataServiceImpl implements TickDataService{

	private TickDataDao tickDataDao;

	public TickDataDao getTickDataDao() {
		return tickDataDao;
	}

	public void setTickDataDao(TickDataDao tickDataDao) {
		this.tickDataDao = tickDataDao;
	}
	
	@Override
	public List<Tick> getTicks(String symbol, LocalDateTime toDatetime, int noOfTicks, String period) throws SQLException{
		return tickDataDao.getTicks(symbol, toDatetime, noOfTicks, period);
	}
	
	@Override
	public Tick getAggregatedTick(String symbol, LocalDateTime fromDatetime, LocalDateTime toDatetime) throws SQLException{
		return tickDataDao.getAggregatedTick(symbol, fromDatetime, toDatetime);
	}

	@Override
	public double[] getBidAsk(String symbol, LocalDateTime datetime) throws SQLException {
		return tickDataDao.getBidAsk(symbol, datetime);
	}
	
	
}

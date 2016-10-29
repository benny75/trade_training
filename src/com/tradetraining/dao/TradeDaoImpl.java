package com.tradetraining.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.tradetraining.model.Trade;
import com.tradetraining.service.TickDataService;

/**
 * @author Benny Wong
 *
 */
public class TradeDaoImpl implements TradeDao
{
	private DataSource dataSource ;

	@Autowired
	private TickDataService tickDataService;
	
	public DataSource getDataSource()
	{
		return this.dataSource;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}


	@Override
	public List<Trade> getTrades(Trade trade) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertTrade(Trade trade) throws SQLException {
		String bidAsk =  (trade.getPosition()==1) ? "ask" : "bid";
		String query ="INSERT INTO trade "+
				"(symbol, username, position, quantity, open_datetime, open_price) "+
				"SELECT ?, ?, ?, ?, ?, " + bidAsk + " from tick where symbol=? and period='1 min' and datetime=?";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, trade.getSymbol());
		pstmt.setString(2, trade.getUser());
		pstmt.setInt(3, trade.getPosition());
		pstmt.setInt(4, trade.getQuantity());
		pstmt.setTimestamp(5, Timestamp.valueOf(trade.getOpenDatetime()));
		pstmt.setString(6, trade.getSymbol());
		pstmt.setTimestamp(7, Timestamp.valueOf(trade.getOpenDatetime()));
		int count = pstmt.executeUpdate();
		dataSource.getConnection().close();
		return (count>0);
	}

	@Override
	public void closeTrade(Trade trade) throws SQLException {

		double[] bidAsk = tickDataService.getBidAsk(trade.getSymbol(), trade.getCloseDatetime());
		
		// Close all buys
		String query ="UPDATE trade SET " +
				"close_datetime = ? , close_price = ? WHERE "+
				"symbol = ? and username = ? and open_datetime <= ? and close_price is null and position=1";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setTimestamp(1, Timestamp.valueOf(trade.getCloseDatetime()));
		pstmt.setDouble(2, bidAsk[0]);
		pstmt.setString(3, trade.getSymbol());
		pstmt.setString(4, trade.getUser());
		pstmt.setTimestamp(5, Timestamp.valueOf(trade.getCloseDatetime()));
		pstmt.executeUpdate();

		// Close all sells
		query ="UPDATE trade SET " +
				"close_datetime = ? , close_price = ? WHERE "+
				"symbol = ? and username = ? and open_datetime <= ? and close_price is null and position=-1";
		pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setTimestamp(1, Timestamp.valueOf(trade.getCloseDatetime()));
		pstmt.setDouble(2, bidAsk[1]);
		pstmt.setString(3, trade.getSymbol());
		pstmt.setString(4, trade.getUser());
		pstmt.setTimestamp(5, Timestamp.valueOf(trade.getCloseDatetime()));
		pstmt.executeUpdate();
		dataSource.getConnection().close();
	}

	public double getOpeningTradesProfit(Trade trade) throws SQLException {
		double result = 0;
		double[] bidAsk = tickDataService.getBidAsk(trade.getSymbol(), trade.getCloseDatetime());
		
		String query ="SELECT open_price, position, quantity  FROM trade WHERE " +
				"symbol = ? and username = ? and open_datetime <= ? and close_price is null";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		int parmI = 1;
		pstmt.setString(parmI++, trade.getSymbol());
		pstmt.setString(parmI++, trade.getUser());
		pstmt.setTimestamp(parmI++, Timestamp.valueOf(trade.getCloseDatetime()));
		ResultSet resultSet = pstmt.executeQuery();
		
		while(resultSet.next()){
			int position = resultSet.getInt(2);
			double closePrice = (position==1) ? bidAsk[1] : bidAsk[0];
			result += (closePrice - resultSet.getDouble(1)) * position * resultSet.getInt(3) / closePrice;
		}
		return result;
	}

	@Override
	public double getOpeningTradesQuantity(Trade trade) throws SQLException {

		String query ="SELECT sum(quantity) FROM trade WHERE " +
				"symbol = ? and username = ? and open_datetime <= ? and close_price is null";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		int parmI = 1;
		pstmt.setString(parmI++, trade.getSymbol());
		pstmt.setString(parmI++, trade.getUser());
		pstmt.setTimestamp(parmI++, Timestamp.valueOf(trade.getCloseDatetime()));
		ResultSet resultSet = pstmt.executeQuery();
		
		if(resultSet.next()){
			return resultSet.getInt(1);
		}
		return 0;
	}
	
	@Override
	public double getNetQuantity(Trade trade) throws SQLException {
		double result = 0;

		String query ="SELECT position * quantity FROM trade WHERE "+
				"symbol = ? and username = ? and open_datetime <= ? and close_price is null";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		int parmI = 1;
		pstmt.setString(parmI++, trade.getSymbol());
		pstmt.setString(parmI++, trade.getUser());
		pstmt.setTimestamp(parmI++, Timestamp.valueOf(trade.getCloseDatetime()));
		ResultSet resultSet = pstmt.executeQuery();
		
		while(resultSet.next()){
			result += resultSet.getDouble(1);
		}
		return result;
	}

	
}
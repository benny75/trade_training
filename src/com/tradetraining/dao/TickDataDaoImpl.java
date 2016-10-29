package com.tradetraining.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import com.tradetraining.model.Tick;

public class TickDataDaoImpl implements  TickDataDao{

	private DataSource dataSource ;

	public DataSource getDataSource()
	{
		return this.dataSource;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	
	@Override
	public List<Tick> getTicks(String symbol, LocalDateTime toDatetime, int noOfTicks, String period)
			throws SQLException {

		LinkedList<Tick> result = new LinkedList<Tick>();
		
		String query = "Select datetime, open, high, low, close from tick where " +
				"symbol = ? and period = ? and datetime <= ? order by datetime desc limit ?";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, symbol);
		pstmt.setString(2, period);
		pstmt.setTimestamp(3, Timestamp.valueOf(toDatetime));
		pstmt.setInt(4, noOfTicks);
		ResultSet resultSet = pstmt.executeQuery();
		
		while(resultSet.next()){
			Tick row = new Tick((LocalDateTime.ofInstant(resultSet.getTimestamp(1).toInstant(), ZoneId.systemDefault())), 
					resultSet.getDouble(2),
					resultSet.getDouble(3),
					resultSet.getDouble(4),
					resultSet.getDouble(5));
			
			result.addFirst(row);
		}
		dataSource.getConnection().close();
		return result;
	}

	@Override
	public Tick getAggregatedTick(String symbol, LocalDateTime fromDatetime, LocalDateTime toDatetime)
			throws SQLException {

		Timestamp openDatetime = null;
		double low = 0;
		double high = 0;
		double open = 0;
		double close = 0;
		
		// Get low and high
		String query = "Select min(low), max(high) from tick where "
				+ "symbol = ? and period = '1 min' and datetime >= ? and datetime <= ? order by datetime asc";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, symbol);
		pstmt.setTimestamp(2, Timestamp.valueOf(fromDatetime));
		pstmt.setTimestamp(3, Timestamp.valueOf(toDatetime));
		ResultSet resultSet = pstmt.executeQuery();
		if (resultSet.next()){
			low = resultSet.getDouble(1);
			high = resultSet.getDouble(2);
		}
		dataSource.getConnection().close();
		// No record retrieved
		if(low==0){
			return null;
		}
		
		// Get openDatetime and open
		query = "Select datetime, open from tick where "
				+ "symbol = ? and period = '1 min' and datetime >= ? and datetime <= ? order by datetime asc limit 1";
		pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, symbol);
		pstmt.setTimestamp(2, Timestamp.valueOf(fromDatetime));
		pstmt.setTimestamp(3, Timestamp.valueOf(toDatetime));
		resultSet = pstmt.executeQuery();
		if (resultSet.next()){
			openDatetime = resultSet.getTimestamp(1);
			open = resultSet.getDouble(2);
		} 
		dataSource.getConnection().close();
			
		// Get close
		query = "Select close from tick where "
				+ "symbol = ? and period = '1 min' and datetime >= ? and datetime <= ? order by datetime desc limit 1";
		pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, symbol);
		pstmt.setTimestamp(2, Timestamp.valueOf(fromDatetime));
		pstmt.setTimestamp(3, Timestamp.valueOf(toDatetime));
		resultSet = pstmt.executeQuery();
		if (resultSet.next()){
			close = resultSet.getDouble(1);
		}
		dataSource.getConnection().close();
		
		return new Tick(LocalDateTime.ofInstant(openDatetime.toInstant(), ZoneId.systemDefault()), open, high, low, close);
	}

	@Override
	public double[] getBidAsk(String symbol, LocalDateTime datetime) throws SQLException {
		String query = "Select bid, ask from tick where symbol = ? and period = '1 min' and datetime <= ? order by datetime desc limit 1";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, symbol);
		pstmt.setTimestamp(2, Timestamp.valueOf(datetime));
		ResultSet resultSet = pstmt.executeQuery();

		if (resultSet.next()){
			double[] result = {resultSet.getDouble(1), resultSet.getDouble(2)};
			return result;
		}
		dataSource.getConnection().close();
		return null;
	}

}

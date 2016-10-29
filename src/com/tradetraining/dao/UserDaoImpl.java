package com.tradetraining.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import com.tradetraining.data.QuoteData;

/**
 * @author Benny Wong
 *
 */
public class UserDaoImpl implements UserDao
{

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
	public boolean isValidUser(String username, String password) throws SQLException
	{
		String query = "Select count(1) from user where username = ? and password = ?";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, username);
		pstmt.setString(2, password);
		ResultSet resultSet = pstmt.executeQuery();
		if(resultSet.next()){
			dataSource.getConnection().close();
			return (resultSet.getInt(1) > 0);
		} else{
			dataSource.getConnection().close();
			return false;
		}
	}

	@Override
	public int getAccountBalance(String username) throws SQLException {

		String query = "Select account_balance from user where username = ?";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, username);
		ResultSet resultSet = pstmt.executeQuery();
		if(resultSet.next()){
			dataSource.getConnection().close();
			return resultSet.getInt(1);
		} else
			dataSource.getConnection().close();
			return 0;

	}

	@Override
	public void updateAccountBalance(String username, double newBalance) throws SQLException {

		String query = "Update user set account_balance = ? where username = ?";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setDouble(1, newBalance);
		pstmt.setString(2, username);
		pstmt.executeUpdate();
	}

	@Override
	public QuoteData getLastSymbolDatetime(String username) throws SQLException {

		String query = "Select last_symbol, last_datetime from user where username = ?";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, username);
		ResultSet resultSet = pstmt.executeQuery();
		if(resultSet.next()){
			dataSource.getConnection().close();
			return new QuoteData(resultSet.getString(1),resultSet.getTimestamp(2));
		} else
			dataSource.getConnection().close();
			return null;
	}

	@Override
	public void setLastSymbolDatetime(String username, String symbol, LocalDateTime datetime) throws SQLException {

		String query = "Update user set last_symbol = ?, last_datetime = ? where username = ?";
		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, symbol);
		pstmt.setTimestamp(2, Timestamp.valueOf(datetime));
		pstmt.setString(3, username);
		pstmt.executeUpdate();
	}

}
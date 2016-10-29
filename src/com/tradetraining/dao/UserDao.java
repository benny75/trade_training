package com.tradetraining.dao;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.tradetraining.model.QuoteData;

/**
 * @author Benny Wong
 * This interface will be used to communicate with the
 * Database
 */
public interface UserDao
{
	public boolean isValidUser(String username, String password) throws SQLException;
	
	public int getAccountBalance(String username) throws SQLException;
	
	public void updateAccountBalance(String username, double newBalance) throws SQLException;

	public QuoteData getLastSymbolDatetime(String username) throws SQLException;

	public void setLastSymbolDatetime(String username, String symbol, LocalDateTime datetime) throws SQLException;
	
}


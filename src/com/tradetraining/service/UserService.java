package com.tradetraining.service;


import java.sql.SQLException;
import java.time.LocalDateTime;

import com.tradetraining.data.QuoteData;

/**
 * @author Benny Wong
 *
 */
public interface UserService
{
	public boolean isValidUser(String username, String password) throws SQLException;
	public double getAccountBalance(String username) throws SQLException;
	public QuoteData getLastSymbolDatetime(String username)throws SQLException;
	public double addAccountBalance(String username, double amountToAdd) throws SQLException;
	public void setLastSymbolDatetime(String username, String symbol, LocalDateTime datetime) throws SQLException;
}
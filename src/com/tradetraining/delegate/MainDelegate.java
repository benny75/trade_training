package com.tradetraining.delegate;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.tradetraining.data.QuoteData;
import com.tradetraining.service.UserService;

public class MainDelegate
{
	private UserService userService;

	public UserService getUserService()
	{
		return this.userService;
	}

	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	public boolean isValidUser(String username, String password) throws SQLException
	{
	    return userService.isValidUser(username, password);
	}
	
	public double getAccountBalance(String username) throws SQLException
	{
	    return userService.getAccountBalance(username);
	}
	
	public QuoteData getLastSymbolDatetime(String username) throws SQLException
	{
	    return userService.getLastSymbolDatetime(username);
	}

	public void setLastSymbolDatetime(String username, String symbol, LocalDateTime datetime) throws SQLException{
		userService.setLastSymbolDatetime(username, symbol, datetime);
	}

	
	
}
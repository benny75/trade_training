package com.tradetraining.service;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.tradetraining.dao.TradeDao;
import com.tradetraining.dao.UserDao;
import com.tradetraining.model.QuoteData;

public class UserServiceImpl implements UserService
{

	private UserDao userDao;
	private TradeDao tradeDao;
	

	public UserDao getUserDao()
	{
		return this.userDao;
	}

	public void setUserDao(UserDao userDao)
	{
		this.userDao = userDao;
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}
	
	@Override
	public boolean isValidUser(String username, String password) throws SQLException
	{
		return userDao.isValidUser(username, password);
	}
	
	@Override
	public double getAccountBalance(String username) throws SQLException
	{
		return userDao.getAccountBalance(username);
	}
	
	@Override
	public QuoteData getLastSymbolDatetime(String username) throws SQLException
	{
		return userDao.getLastSymbolDatetime(username);
	}

	@Override
	public double addAccountBalance(String username, double amountToAdd) throws SQLException {
		double balance = userDao.getAccountBalance(username) + amountToAdd;
		userDao.updateAccountBalance(username, balance);
		return balance;
	}

	@Override
	public void setLastSymbolDatetime(String username, String symbol, LocalDateTime datetime) throws SQLException {
		userDao.setLastSymbolDatetime(username, symbol, datetime);
		
	}

}

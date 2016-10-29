package com.tradetraining.model;

import java.time.LocalDateTime;

public class Trade {

	private String symbol;
	
	private String user;
	
	// Buy = 1, sell = -1
	private int position;
	
	private int quantity;
	
	// Trade executed time
	private LocalDateTime openDatetime;
	
	// Price when open 
	private double openPrice;

	// Trade executed time
	private LocalDateTime closeDatetime;
	
	// Price when open 
	private double closePrice;


	/**
	 * Constructor for new empty trade
	 **/
	public Trade(){
	}
	
	/**
	 * Constructor for new trade
	 **/
	public Trade(String user, String symbol, int position, int quantity, LocalDateTime openDatetime){
		this.setUser(user);
		this.setSymbol(symbol);
		this.setPosition(position);
		this.setQuantity(quantity);
		this.setOpenDatetime(openDatetime);
	}

	/**
	 * Constructor for close trade
	 **/
	public Trade(String user, String symbol, LocalDateTime closeDatetime){
		this.setUser(user);
		this.setSymbol(symbol);
		this.setCloseDatetime(closeDatetime);
	}

	/**
	 * Constructor for passing a whole trade record
	 **/
	public Trade(String user, String symbol, int position, int quantity, LocalDateTime openDatetime, double openPrice, LocalDateTime closeDatetime, double closePrice){
		this.setUser(user);
		this.setSymbol(symbol);
		this.setPosition(position);
		this.setQuantity(quantity);
		this.setOpenDatetime(openDatetime);
		this.setOpenPrice(openPrice);
		this.setCloseDatetime(closeDatetime);
		this.setClosePrice(closePrice);
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getOpenDatetime() {
		return openDatetime;
	}

	public void setOpenDatetime(LocalDateTime openDatetime) {
		this.openDatetime = openDatetime;
	}

	public double getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}

	public LocalDateTime getCloseDatetime() {
		return closeDatetime;
	}

	public void setCloseDatetime(LocalDateTime closeDatetime) {
		this.closeDatetime = closeDatetime;
	}

	public double getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}



}

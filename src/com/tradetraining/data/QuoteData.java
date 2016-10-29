package com.tradetraining.data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Store and pass symbol and datetime with DAO
 * 
 * @author Benny Wong
 *
 */
public class QuoteData {

	private String symbol;
	private LocalDateTime datetime;
	
	public QuoteData(String symbol, LocalDateTime datetime){
		this.symbol = symbol;
		this.datetime = datetime;
	}
	
	//Constructor for sql.timestamp input
	public QuoteData(String symbol, Timestamp date) {
		this.symbol = symbol;
		this.datetime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public LocalDateTime getDatetime() {
		return datetime;
	}
	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	} 
	
}

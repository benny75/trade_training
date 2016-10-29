package com.tradetraining.data;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Tick {
	
//	private LocalDateTime datetime; 
	private long datetime; 
	private double open, high, low, close;
	private final ZoneId zoneId = ZoneId.systemDefault();
	
	public Tick(){}
	
	public Tick(LocalDateTime datetime, double open, double high, double low, double close){
		setDatetime(datetime);
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}

	public Tick(long datetime, double open, double high, double low, double close){
		setDatetime(datetime);
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}

	public long getDatetime() {
		return datetime;
	}
	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}
	
	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime.atZone(zoneId).toEpochSecond();
	}
	
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	} 
	
}

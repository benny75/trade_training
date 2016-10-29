package com.tradetraining.data;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tradetraining.service.TickDataService;


@Controller
@RequestMapping("/feeddata")
public class FeedDataJsonServlet {

	@Autowired
	private TickDataService tickDataService;

	private final int NOOFTICKS = 61;

	@RequestMapping(value="{period}", method = RequestMethod.GET)
	public @ResponseBody List<Tick> handleRequest(HttpServletRequest request, @PathVariable String period) {
		
		String symbol = (String) request.getSession().getAttribute("symbol");
		LocalDateTime currentDatetime = (LocalDateTime) request.getSession().getAttribute("datetime");
		LocalDateTime toDatetime = adjustToDatetime(currentDatetime, period);
		List<Tick> rows = null;
		// Fill the data table.
		try {

			// Get ticks data (excluding the last tick) from DB
			rows = tickDataService.getTicks(symbol, toDatetime, NOOFTICKS-1, period);

			// Evaluate the last tick
			Tick row = tickDataService.getAggregatedTick(symbol, toDatetime, currentDatetime);
			if (row != null){
				((LinkedList<Tick>) rows).addLast(row);
			}
			
		} catch (SQLException e) {
			System.out.println("SQL error");
			e.printStackTrace();

		} 
		return rows;
		
	}

	// Return the end time of the second last tick
	private LocalDateTime adjustToDatetime(LocalDateTime datetime, String period) {
		switch (period){
		case "1 day":
			return datetime.withHour(0).withMinute(0).withSecond(0);
		case "1 hour":
			return datetime.withMinute(0).withSecond(0);
		case "4 hours":
			int hour = (datetime.getHour() / 4) * 4;
			return datetime.withHour(hour).withMinute(0).withSecond(0);
		default:			// no adjustment needed for 1 min
			return datetime;
		}
	}


}

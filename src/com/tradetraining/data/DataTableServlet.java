package com.tradetraining.data;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import com.google.visualization.datasource.DataSourceHelper;
import com.google.visualization.datasource.DataSourceRequest;
import com.google.visualization.datasource.base.DataSourceException;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.DateTimeValue;
import com.tradetraining.service.TickDataService;

public class DataTableServlet extends HttpServlet implements HttpRequestHandler{
	private static final long serialVersionUID = 5943083770990760733L;

	@Autowired
	private TickDataService tickDataService;

	private final int NOOFTICKS = 60;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DataTable data = new FeedDataTable();
		DataSourceRequest dsRequest = null;

		String symbol = (String) request.getSession().getAttribute("symbol");
		LocalDateTime currentDatetime = (LocalDateTime) request.getSession().getAttribute("datetime");
		String period = request.getParameter("period");
		LocalDateTime toDatetime = adjustToDatetime(currentDatetime, period);

		// Fill the data table.
		try {
			// Extract the datasource request parameters.
			dsRequest = new DataSourceRequest(request);

			// Get ticks data (excluding the last tick) from DB
			List<Tick> rows = tickDataService.getTicks(symbol, toDatetime, NOOFTICKS-1, period);
			for (Tick t : rows){
				data.addRow(tickToTableRow(t));
			}
			// Evaluate the last tick
			Tick row = tickDataService.getAggregatedTick(symbol, toDatetime, currentDatetime);
			if (row != null){
				data.addRow(tickToTableRow(row));
			}

			// Set the response.
			DataSourceHelper.setServletResponse(data, dsRequest, response);

		} catch (SQLException e) {
			System.out.println("SQL error");
			e.printStackTrace();

		} catch (DataSourceException e) {
			if (dsRequest != null) {
				DataSourceHelper.setServletErrorResponse(e, dsRequest, response);
			} else {
				DataSourceHelper.setServletErrorResponse(e, request, response);
			}
		} 
	}

	@SuppressWarnings("deprecation")
	private TableRow tickToTableRow(Tick t) {

		TableRow row = new TableRow();

		Date datetime = new Date(t.getDatetime());
		int year = datetime.getYear();
		int month = datetime.getMonth();
		int dayOfMonth = datetime.getDate();
		int hour = datetime.getHours();
		int minute = datetime.getMinutes();
		row.addCell(new TableCell(new DateTimeValue(year, month, dayOfMonth, hour, minute, 0, 0)));
		row.addCell(new TableCell(t.getLow()));
		row.addCell(new TableCell(t.getOpen()));
		row.addCell(new TableCell(t.getClose()));
		row.addCell(new TableCell(t.getHigh()));
		return row;
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

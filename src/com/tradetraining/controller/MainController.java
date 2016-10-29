package com.tradetraining.controller;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tradetraining.data.QuoteData;
import com.tradetraining.data.Trade;
import com.tradetraining.delegate.MainDelegate;
import com.tradetraining.service.TickDataService;
import com.tradetraining.service.TradeService;
import com.tradetraining.service.TradeUnexecutedException;
import com.tradetraining.viewBean.MainBean;


@Controller
public class MainController
{
	@Autowired
	private MainDelegate mainDelegate;
	
	@Autowired
	private TickDataService tickDataService;
	
	@Autowired
	private TradeService tradeService;

	@RequestMapping(value="/main",method=RequestMethod.GET)
	public ModelAndView displayMain(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		ModelAndView model = new ModelAndView("main");
		MainBean mainBean = new MainBean();
		model.addObject("mainBean", mainBean);

		// Get user from session. Redirect to login page if no logged in
		String loggedInUser = (String)session.getAttribute("loggedInUser");
		System.out.println("user: " + loggedInUser);
		if(loggedInUser==null){
			model = new ModelAndView("redirect:/login");
			request.setAttribute("message", "Not logged in!!"); 
			return model;
		}

		try{
			// Load user info from DB
			double accountBalance =  mainDelegate.getAccountBalance(loggedInUser);
			QuoteData initSymbolDatetime = mainDelegate.getLastSymbolDatetime(loggedInUser);
			double[] bidAsk = tickDataService.getBidAsk(initSymbolDatetime.getSymbol(), initSymbolDatetime.getDatetime());
			
			updateSessionPositionInfo(session, loggedInUser, initSymbolDatetime.getSymbol(), initSymbolDatetime.getDatetime());
			
			// Set attributes to view and session
			session.setAttribute("accountBalance",accountBalance);
			session.setAttribute("symbol", initSymbolDatetime.getSymbol());
			session.setAttribute("rollDuration", 1);
			session.setAttribute("datetime", initSymbolDatetime.getDatetime());
			session.setAttribute("day", initSymbolDatetime.getDatetime().getDayOfWeek().toString());
			session.setAttribute("bid", bidAsk[0]);
			session.setAttribute("ask", bidAsk[1]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


		return model;
	}


	@RequestMapping(value="/main",method=RequestMethod.POST)
	public ModelAndView executeMain(HttpServletRequest request, HttpServletResponse response,HttpSession session, @ModelAttribute("mainBean")MainBean mainBean, @RequestParam String action)
	{
		ModelAndView model= null;
		try
		{
			session.setAttribute("message", "");

			// Get user from session. Redirect to login page if no logged in
			String loggedInUser = (String)session.getAttribute("loggedInUser");
			if(loggedInUser==null){
				model = new ModelAndView("redirect:/login");
				request.setAttribute("message", "Not logged in!!"); 
				return model;
			}

			LocalDateTime datetime = (LocalDateTime) session.getAttribute("datetime");
			String symbol = (String)session.getAttribute("symbol");

			try{

				DecimalFormat df = new DecimalFormat("#.####");
				df.setRoundingMode(RoundingMode.HALF_EVEN);
				// Buy, Sell, Close position
				double accountBalance;
				switch(action){
				case "Buy":
					accountBalance = tradeService.openTrade(new Trade(
							loggedInUser, symbol, 
							1, mainBean.getQuantity(), 
							datetime));
					
					// Update account balance
					session.setAttribute("accountBalance",df.format(accountBalance));
					break;
					
				case "Sell":
					accountBalance = tradeService.openTrade(new Trade(
							loggedInUser, symbol, 
							-1, mainBean.getQuantity(), 
							datetime));
					//Update account balance
					session.setAttribute("accountBalance",df.format(accountBalance));
					break;
					
				case "Close":
					accountBalance = tradeService.closeTrade(new Trade(
							loggedInUser, symbol, datetime));
					//Update account balance
					session.setAttribute("accountBalance",df.format(accountBalance));
					break;
				}

			} catch(TradeUnexecutedException e){
				session.setAttribute("message", "Trade not executed. (non-trading hours?)");
			}
			
			//Update datetime with roll duration
			datetime = datetime.plusMinutes(mainBean.getRollDuration());
			mainDelegate.setLastSymbolDatetime(loggedInUser, symbol, datetime);
			session.setAttribute("datetime", datetime);
			session.setAttribute("day", datetime.getDayOfWeek().toString());
			session.setAttribute("rollDuration", mainBean.getRollDuration());

			// Update current profit
			updateSessionPositionInfo(session, loggedInUser, symbol, datetime);
			
			double[] bidAsk = tickDataService.getBidAsk(symbol, datetime);
			session.setAttribute("bid", bidAsk[0]);
			session.setAttribute("ask", bidAsk[1]);

			model = new ModelAndView("main");
			model.addObject("mainBean", mainBean);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return model;
	}


	private void updateSessionPositionInfo(HttpSession session, String loggedInUser, String symbol,
			LocalDateTime datetime) throws SQLException {

		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		Trade current = new Trade(loggedInUser, symbol, datetime);
		double netQuantity = tradeService.getNetQuantity(current);
		double netProfit = tradeService.getOpeningTradesProfit(current);
		if ((netQuantity>0)){
			session.setAttribute("net_position", "Long");
			session.setAttribute("net_quantity", Math.abs(netQuantity));
			session.setAttribute("net_profit", df.format(netProfit));
		} else if((netQuantity<0)){
			session.setAttribute("net_position", "Short");
			session.setAttribute("net_quantity", Math.abs(netQuantity));
			session.setAttribute("net_profit", df.format(netProfit));
		} else {
			session.setAttribute("net_position", "No opened position");
			session.setAttribute("net_quantity", "");
			session.setAttribute("net_profit", "");
		}
		
	}
}

<%@include file="include.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>

body {
    font-family: Verdana, Arial, Helvetica, sans-serif;
    background: #000;
    color:#B190D4;
}

#chartBox{
    margin: auto;
    padding: 20px 20px 20px 20px;
    width: 1150px;
    display:inline-block;
    background: #5D3289;
    border-radius: 10px;
    border: 1px solid #91879C;
    float: right;
}

.sub-info{
    padding: 10px 10px 10px 10px;
    width: 400px;
    margin: 0 auto;
    background: #5D3289;
    border-radius: 10px;
    border: 1px solid #91879C;
    margin: 5px;
}

.info{
    width: 400px;
    float: left;
}

.chart{
    display:inline-block;
    box-sizing: border-box;
    width: 560px;
    height: 335px;
    }
    
</style>

<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript">
	// Load the Visualization API and the ready-made Google table visualization.
	google.load('visualization', '1', {'packages':['table,corechart']});
	google.setOnLoadCallback(redrawCharts);
	
	function init(){
		calculateMaxQuantity();
	}
	
	// Update all the charts when button is pressed    
	function redrawCharts() {
		// Update charts
		var query = new google.visualization.Query("datatableservlet?period=1 day");
		query.send(handle1DResponse);
		var query = new google.visualization.Query("datatableservlet?period=4 hours");
		query.send(handle4HResponse);
		var query = new google.visualization.Query("datatableservlet?period=1 hour");
		query.send(handle1HResponse);
		var query = new google.visualization.Query("datatableservlet?period=1 min");
		query.send(handle1MResponse);

	}

	// Draw 1 day chart
	function handle1DResponse(response, chart) {
		var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div_1d'));
		var data = response.getDataTable();
		// charting options
	    var options = {
	      legend: 'none',
	      bar: { groupWidth: '100%' }, // Remove space between bars.
	      candlestick: {
	    	hollowIsRising: 'true',
	        fallingColor: { strokeWidth: 0, fill: '#a52714' }, // red
	        risingColor: { strokeWidth: 0, fill: '#0f9d58' }   // green
	      },
	      'title':'1 Day'
	    };
		chart.draw(data, options);
	}
	// Draw 4 hours chart
	function handle4HResponse(response, chart) {
		var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div_4h'));
		var data = response.getDataTable();
		// charting options
	    var options = {
	      legend: 'none',
	      bar: { groupWidth: '100%' }, // Remove space between bars.
	      candlestick: {
	    	hollowIsRising: 'true',
	        fallingColor: { strokeWidth: 0, fill: '#a52714' }, // red
	        risingColor: { strokeWidth: 0, fill: '#0f9d58' }   // green
	      },
	      'title':'4 Hours'
	    };
		chart.draw(data, options);
	}
	// Draw 1 hour chart
	function handle1HResponse(response, chart) {
		var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div_1h'));
		var data = response.getDataTable();
		// charting options
	    var options = {
	      legend: 'none',
	      bar: { groupWidth: '100%' }, // Remove space between bars.
	      candlestick: {
	    	hollowIsRising: 'true',
	        fallingColor: { strokeWidth: 0, fill: '#a52714' }, // red
	        risingColor: { strokeWidth: 0, fill: '#0f9d58' }   // green
	      },
	      'title':'1 Hour'
	    };
		chart.draw(data, options);
	}
	// Draw 1 min chart
	function handle1MResponse(response, chart) {
		var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div_1m'));
		var data = response.getDataTable();
		// charting options
	    var options = {
	      legend: 'none',
	      bar: { groupWidth: '100%' }, // Remove space between bars.
	      candlestick: {
	    	hollowIsRising: 'true',
	        fallingColor: { strokeWidth: 0, fill: '#a52714' }, // red
	        risingColor: { strokeWidth: 0, fill: '#0f9d58' }   // green
	      },
	      'title':'1 Minute'
	    };
		chart.draw(data, options);
	}

	// Update rollDuration value shown
	function showVal(newVal) {
		//var string = "Roll ";
		//document.getElementById("rollDurationValue").innerHTML = string.concat(newVal, " minutes"); 
		document.getElementById("rollDurationValue").innerHTML = newVal; 
	}
	
	// Find the max quantity can by opened
	function calculateMaxQuantity(){
		document.getElementById("maxOpen").innerHTML = Math.floor(${accountBalance} / ${ask}); 
	}
	
	function openAction(){
		if(validateQuantity()){
			redrawCharts();
		} else {
			return false;
		}
	}

	function validateQuantity() {
	    var quantity = document.forms["rolltimeForm"]["quantity"].value;
	    if ( quantity > ${accountBalance}){
	        alert("Insufficient fund");
	        return false;
	    } 
	    if(quantity <= 0){
	        alert("No position to be opened");
	        return false;
	    }
		return true;
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
</head>
<body onload="init();">
	<div class="info">
		<div class="sub-info">
			<p>User ${loggedInUser}</p>	
			<p>Account balance: ${accountBalance}</p>
			<p>Datetime: ${datetime}</p>
			<p>Current Position: ${net_position} ${net_quantity} ${net_profit}</p>
		</div>

		<div class="sub-info">
			<form:form id="rolltimeForm" method="post" action="main" modelAttribute="mainBean">
				<form:input type="range" path="rollDuration" id="rollDuration"
					oninput="showVal(this.value)" min="1" max="1000"
					value="${rollDuration}" />
				Roll <span id="rollDurationValue">  ${rollDuration} </span> minutes
				<form:input type="text" path="quantity" id="quantity" name="quantity"/>
				<br>
				<input type="submit" name="action" value="Roll time" onclick="redrawCharts()" />
				<input type="submit" name="action" value="Buy" onclick="return openAction()"/>
				<input type="submit" name="action" value="Sell" onclick="return openAction()"/>
				<input type="submit" name="action" value="Close" onclick="redrawCharts()"/>
			</form:form>
		</div>
		
		<div class="sub-info">Symbol: ${symbol}. Bid: ${bid}. Ask: ${ask}</div>
		<div class="message">${message }</div>
		
	</div>
	
	
	<div id="chartBox">
		<div class="chart" id="chart_div_1m">1 min</div>
		<div class="chart" id="chart_div_1h" >1 hour</div>
		<div class="chart" id="chart_div_4h" >4 hours</div>
		<div class="chart" id="chart_div_1d">1 day</div>
	</div>
	
	
</body>
</html>

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

g{
	font-size: 85%;
}

.chart{
    display:inline-block;
    box-sizing: border-box;
    width: 560px;
    height: 335px;
    }
    
    .axis path,
    .axis line {
        fill: none;
        stroke: #000;
        shape-rendering: crispEdges;
    }

    path.candle {
        stroke: #000000;
    }

    path.candle.body {
        stroke-width: 0;
    }

    path.candle.up {
        fill: #00AA00;
        stroke: #00AA00;
    }

    path.candle.down {
        fill: #FF0000;
        stroke: #FF0000;
    }
    
</style>

<script src="http://d3js.org/d3.v3.min.js"></script>
<script src="http://techanjs.org/techan.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
</head>
<body>
	<div class="info">
		<div class="sub-info">
			<p>User ${loggedInUser}</p>	
			<p>Account balance: ${accountBalance}</p>
			<p>Datetime: ${datetime}, ${day }</p>
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
	
	
<script type="text/javascript">

	var margin = {top: 20, right: 20, bottom: 30, left: 50},
	        width = 560 - margin.left - margin.right,
	        height = 335 - margin.top - margin.bottom;
	
	var parseDate = d3.time.format("%d-%b-%y").parse;
	
	var x = techan.scale.financetime()
	        .range([0, width]);
	
	var y = d3.scale.linear()
	        .range([height, 0]);
	
	var candlestick = techan.plot.candlestick()
	        .xScale(x)
	        .yScale(y);
	
	var xAxis = d3.svg.axis()
	        .scale(x)
	        .orient("bottom");
	
	var yAxis = d3.svg.axis()
	        .scale(y)
	        .orient("left");
	
	var svg1d = d3.select("#chart_div_1d").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	var svg4h = d3.select("#chart_div_4h").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	var svg1h = d3.select("#chart_div_1h").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	var svg1m = d3.select("#chart_div_1m").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	
	var feedData;
	
	redrawCharts();
	
	function redrawCharts(){

		d3.json("feeddata/1 day/", function(error, data) {
			
			if (error) return console.warn(error);
			feedData = data;
			drawChart(svg1d);
		});
		
		d3.json("feeddata/4 hours/", function(error, data) {
			
			if (error) return console.warn(error);
			feedData = data;
			drawChart(svg4h);
		});
		
		d3.json("feeddata/1 hour/", function(error, data) {
			
			if (error) return console.warn(error);
			feedData = data;
			drawChart(svg1h);
		});
		
		d3.json("feeddata/1 min/", function(error, data) {
			
			if (error) return console.warn(error);
			feedData = data;
			drawChart(svg1m);
		});
	};
	
	
	function drawChart(svg){

	    var accessor = candlestick.accessor();
	    
	    feedData = feedData.slice(0, 60).map(function(d) {
	        return {
	            date: new Date(d.datetime*1000),
	            open: +d.open,
	            high: +d.high,
	            low: +d.low,
	            close: +d.close
	         };
	    }).sort(function(a, b) { return d3.ascending(accessor.d(a), accessor.d(b)); });


		x.domain(feedData.map(accessor.d));
	    y.domain(techan.scale.plot.ohlc(feedData, accessor).domain());

	    svg.append("g")
	            .datum(feedData)
	            .attr("class", "candlestick")
	            .call(candlestick);

	    svg.append("g")
	            .attr("class", "x axis")
	            .attr("transform", "translate(0," + height + ")")
	            .call(xAxis);

	    svg.append("g")
	            .attr("class", "y axis")
	            .call(yAxis)
	            .append("text")
	            .attr("transform", "rotate(-90)")
	            .attr("y", 6)
	            .attr("dy", ".71em")
	            .style("text-anchor", "end")
	            .text("Price ($)");

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
</body>
</html>

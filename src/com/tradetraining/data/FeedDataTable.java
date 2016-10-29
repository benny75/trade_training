package com.tradetraining.data;

import java.util.ArrayList;

import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.ValueType;

public class FeedDataTable extends DataTable {

	public FeedDataTable(){
		super();

	    ArrayList<ColumnDescription> cd = new ArrayList<ColumnDescription>();
	    cd.add(new ColumnDescription("datetime", ValueType.DATETIME, "Datetime"));
	    cd.add(new ColumnDescription("low", ValueType.NUMBER, "low"));
	    cd.add(new ColumnDescription("open", ValueType.NUMBER, "open"));
	    cd.add(new ColumnDescription("close", ValueType.NUMBER, "close"));
	    cd.add(new ColumnDescription("high", ValueType.NUMBER, "high"));

	    this.addColumns(cd);
	}
	
}

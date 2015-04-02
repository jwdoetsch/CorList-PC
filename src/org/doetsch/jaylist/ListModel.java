package org.doetsch.jaylist;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

class ListModel {
	
	private URL path;
	private String header;
	private ArrayList<ItemModel> itemModels;
	
	ListModel (String header) {
		this.header = header;
		this.itemModels = new ArrayList<ItemModel>();
	}
	
	ListModel () {
		this.itemModels = new ArrayList<ItemModel>();
	}
	
	void addItemModels (ItemModel model) {
		itemModels.add(model);
	}
	
	void setPath (URL path) {
		this.path = path;
	}
	
	URL getPath () {
		return this.path;
	}
	
	String getHeader () {
		return this.header;
	}
	
	void setHeader (String header) {
		this.header = header;
	}
	
	ArrayList<ItemModel> getItemModels () {
		return this.itemModels;
	}

}
